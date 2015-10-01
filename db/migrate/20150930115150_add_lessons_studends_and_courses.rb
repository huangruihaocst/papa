class AddLessonsStudendsAndCourses < ActiveRecord::Migration
  def change

    create_table :courses do |t|
      t.string  :name
      t.text    :description
    end

    create_table :lessons do |t|
      t.string  :name,        null: false
      t.text    :description
      t.datetime  :start_time
      t.datetime  :end_time
      t.string  :position
      t.integer :course_id
    end

    create_table :participations do |t|
      t.integer :course_id
      t.integer :user_id
      t.string  :role
    end

  end
end
