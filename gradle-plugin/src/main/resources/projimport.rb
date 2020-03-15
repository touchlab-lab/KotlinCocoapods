require 'xcodeproj'

project_file = ARGV[0]
targetName = ARGV[1]
groupName = ARGV[2]

project = Xcodeproj::Project.open(project_file)

target = project.targets.find {|target| target.name == targetName}

if target == nil
  puts "target #{targetName} not found"
  exit(false)
end

kot_group = project.groups.find do |group|
  group.name == groupName
end

if kot_group == nil
  kot_group = project.new_group(groupName)
end

kt_files = kot_group.recursive_children.select do |elem|
  elem.kind_of? Xcodeproj::Project::Object::PBXFileReference
end.map do |file_ref|
  file_ref.real_path.to_s
end.select do |path|
  path.end_with?(".kt") and File.exists?(path)
end

group_index = {}

def dlog(str)
  if false
    puts str
  end
end

def walkGroups (group_index, pathBase, groups)
  groups.each do |group|
    if group.name != nil
      groupPathName = pathBase + '/' + group.name
      group_index[groupPathName] = group
      walkGroups(group_index, groupPathName, group.groups)
    end
  end
end

walkGroups(group_index, "", kot_group.groups)

def addfiles (existingFiles, group_index, direc, pathBase, current_group, main_target)

    Dir.glob(direc).sort.each do |item|
        next if item == '.' or item == '.DS_Store'
        new_folder = File.basename(item)

        if File.directory?(item)

          groupPathName = pathBase + '/' + new_folder
          foundGroup = group_index[groupPathName]
          if foundGroup == nil
            foundGroup = current_group.new_group(new_folder)
            group_index[groupPathName] = foundGroup
            dlog "creating #{groupPathName}"
          else
            dlog "existing #{groupPathName}"
          end
          addfiles(existingFiles, group_index, "#{item}/*", groupPathName, foundGroup, main_target)
        else

          if item.end_with? ".kt"
            projectPath = "#{pathBase}/#{new_folder}"
            fileFound = existingFiles.any? { |path|
              path.end_with? projectPath
            }
            if fileFound
              dlog "File #{projectPath} exists"
            else
              dlog "File #{projectPath} created"
              current_group.new_file(item)
            end
          end
        end
    end
end

srcDirIndex = 3

while srcDirIndex < ARGV.length do
  importPath = ARGV[srcDirIndex]
  addfiles(kt_files, group_index, "#{importPath}/*", "", kot_group, target)
  srcDirIndex +=1
end

project.save(project_file)
