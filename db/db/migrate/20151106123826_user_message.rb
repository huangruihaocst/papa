class UserMessage < ActiveRecord::Migration
  def change
    create_table :user_messages do |t|
      t.integer :sender_id
      t.integer :receiver_id
      t.string  :title
      t.text    :content
      t.string  :status
      t.boolean   :sender_deleted, null: false, default: false
      t.boolean   :receiver_deleted, null: false, default: false
      t.timestamps null: false
    end
  end
end
