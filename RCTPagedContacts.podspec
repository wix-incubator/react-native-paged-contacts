require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "RCTPagedContacts"
  s.version      = "1.0.0"
  s.summary      = "Paged contacts for React Native"
  s.license      = "MIT"

  s.authors      = "Wix"
  s.homepage     = "https://github.com/wix/react-native-paged-contacts"
  s.platform     = :ios, "10.0"

  s.source       = { :git => "https://github.com/wix/react-native-paged-contacts.git", :tag => "v#{s.version}" }
  s.source_files  = "ios/**/*.{h,m}"

  s.dependency 'React'
end
