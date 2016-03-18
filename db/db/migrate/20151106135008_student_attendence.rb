class StudentAttendence < ActiveRecord::Migration
  def change
    create_table :student_attendences do |t|
      t.integer   :user_id
      t.integer   :lesson_id
      t.string    :sign_up_method
      t.text      :description
      t.timestamps null: false
    end
  end
end
