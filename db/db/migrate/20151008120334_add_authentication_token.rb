class AddAuthenticationToken < ActiveRecord::Migration
  def change
    create_table :tokens do |t|
      t.string    :token, null: false
      t.integer   :user_id, null: false
      t.datetime  :valid_until, null: false
    end
  end
end
