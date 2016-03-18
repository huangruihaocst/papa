class CourseBuilder

  LESSON_COUNT = 8

  def initialize
    f = open('db/init_data/courses', 'r')
    @courses = []
    f.readlines.each do |line|
      columns = line.split
      if columns.size > 1
        @courses.push(columns[1])
      end
    end
  end

  def build
    index = Random.rand(@courses.size)
    course = @courses[index]
    @courses.delete_at(index)
    course
  end

  LESSON_KINDS = %w'第%d周习题课 第%d周课程 第%d周实验课'

  def self.build_lessons
    lessons = []
    lesson_str = LESSON_KINDS[Random.rand(LESSON_KINDS.size)]
    LESSON_COUNT.times do |x|
       lessons.push(lesson_str % (x+1))
    end
    lessons
  end

end