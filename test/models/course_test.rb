require 'test_helper'

class CourseTest < ActiveSupport::TestCase
  test 'should have multiple courses' do
    assert Course.count > 0
  end

end
