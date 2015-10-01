class AddTeachers < ActiveRecord::Migration
  def change
    create_table :teachers do |t|
      t.string  :name, null: false
      t.string  :encrypted_password
    end

    create_table :teachers_courses do |t|
      t.integer :teacher_id
      t.integer :course_id
    end
  end
end
