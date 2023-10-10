require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name           = package['name']
  s.version        = package['version']
  s.summary        = package['description']
  s.description    = package['description']
  s.license        = package['license']
  s.author         = package['author']
  s.homepage       = "https://github.com/wix/react-native-paged-contacts"
  s.source         = { git: 'https://github.com/wix/react-native-paged-contacts' }

  s.requires_arc   = true
  s.platform       = :ios, '8.0'

  s.preserve_paths = 'package.json', 'index.js'
  s.source_files   = 'ios/**/*.{h,m}'

  s.dependency 'React/Core'
end
