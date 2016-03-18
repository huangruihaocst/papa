class AddLessonsStudendsAndCourses < ActiveRecord::Migration
  def change

    create_table :semesters do |t|
      t.string :name
    end

    create_table :courses do |t|
      t.string  :name,        null: false
      t.text    :description
      t.integer :semester_id
      t.timestamps
    end

    create_table :lessons do |t|
      t.string  :name,        null: false
      t.text    :description
      t.datetime  :start_time
      t.datetime  :end_time
      t.string  :location
      t.integer :course_id,   null: false
      t.timestamps
    end

    create_table :course_files do |t|
      t.integer :course_id
      t.integer :file_resource_id
    end

    create_table :lesson_files do |t|
      t.integer :lesson_id
      t.integer :file_resource_id
    end

    create_table :student_files do |t|
      t.integer :student_id
      t.integer :lesson_id
      t.integer :file_resource_id
      t.integer :creator_id
    end

    create_table :assistant_files do |t|
      t.integer :assistant_id
      t.integer :lesson_id
      t.integer :file_resource_id
    end

    create_table :participations do |t|
      t.integer :course_id
      t.integer :user_id
      t.string  :role, default: ROLE_STUDENT # student/assistant
    end
    add_index :participations, :course_id
    add_index :participations, :user_id

    create_table :teaching_courses do |t|
      t.integer :user_id
      t.integer :course_id
    end

    create_table :lesson_statuses do |t|
      t.string :score
      t.integer :user_id
      t.integer :lesson_id
      t.integer :creator_id
      t.timestamps
    end

    create_table :lesson_comments do |t|
      t.text :content
      t.string :score
      t.integer :creator_id
      t.integer :lesson_id
      t.timestamps
    end

    create_table :student_comments do |t|
      t.text :content
      t.string :score
      t.integer :creator_id
      t.integer :lesson_id
      t.integer :student_id
      t.timestamps
    end

    create_table :messages do |t|
      t.string :message_type
      t.string :title
      t.datetime :deadline
      t.text :content
      t.integer :creator_id
      t.integer :course_id
      t.timestamps
    end

    create_table :file_resources do |t|
      t.string :file_type
      t.string :name
      t.string :path
      t.integer :creator_id
      t.timestamps
    end

    create_table :android_apps do |t|
      t.string :version
      t.integer :file_resource_id
      t.timestamps
    end

  end
end
