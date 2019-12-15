require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "RCTPagedContacts"
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']

  s.authors      = "Wix"
  s.homepage     = package['homepage']
  s.platform     = :ios, "10.0"

  s.source       = { :git => "https://github.com/wix/react-native-paged-contacts.git", :tag => "v#{s.version}" }
  s.source_files  = "ios/**/*.{h,m}"

  s.dependency 'React'
end
