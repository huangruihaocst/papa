# require 'base64'
#
# class UploadFile < ActiveRecord::Base
#   validates :path, presence: true
#   validates_associated :user
#
#   belongs_to :user
#
#   def self.file_name_to_path(file_name)
#     suffix = file_name.split('.')[-1]
#     if ALLOWED_FILE_SUFFIXES.include?(suffix)
#
#     Base64.encode64(file_name)
# end
