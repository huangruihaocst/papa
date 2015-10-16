ENV['RAILS_ENV'] ||= 'test'
require File.expand_path('../../config/environment', __FILE__)
require 'rails/test_help'

class ActiveSupport::TestCase
  # Setup all fixtures in test/fixtures/*.yml for all tests in alphabetical order.
  fixtures :all

  def json
    @json = JSON.parse(response.body) unless @json
    @json
  end

  def assert_json_success
    assert_equal STATUS_SUCCESS, json['status']
  end


  # Add more helper methods to be used by all tests here...
end
