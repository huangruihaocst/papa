class AddLessonsStudendsAndCourses < ActiveRecord::Migration
  def change

    create_table :courses do |t|
      t.string  :name,        null: false
      t.text    :description

      t.integer :semester_id
    end

    create_table :lessons do |t|
      t.string  :name,        null: false
      t.text    :description
      t.datetime  :start_time
      t.datetime  :end_time
      t.string  :location
      t.integer :course_id,   null: false
    end

    create_table :participations do |t|
      t.integer :course_id
      t.integer :user_id
      t.string  :role, default: ROLE_STUDENT # student/assitant
    end

    create_table :teaching_courses do |t|
      t.integer :user_id
      t.integer :course_id
    end

    create_table :assisting_courses do |t|
      t.integer :user_id
      t.integer :course_id
    end

    create_table :learning_courses do |t|
      t.integer :user_id
      t.integer :course_id
    end

    create_table :lesson_statuses do |t|
      t.string :score
      t.integer :user_id
      t.integer :lesson_id
      t.timestamps
    end

    create_table :lesson_remarks do |t|
      t.text :content
      t.integer :score
      t.integer :creator_id
      t.integer :lesson_id
      t.timestamps
    end

    create_table :student_remarks do |t|
      t.text :content
      t.integer :score
      t.integer :creator_id
      t.integer :lesson_id
      t.integer :student_id
      t.timestamps
    end

    create_table :messages do |t|
      t.string :type
      t.string :title
      t.datetime :deadline
      t.text :content
      t.integer :creator_id
      t.integer :course_id
      t.timestamps
    end

    create_table :files do |t|
      t.string :type
      t.string :name
      t.string :path
      t.integer :creator_id
      t.timestamps
    end

    create_table :android_apps do |t|
      t.string :version
      t.integer :file_id
      t.timestamps
    end

  end
end
