class AddUploadResourceManager < ActiveRecord::Migration
  def change
    create_table :upload_files do |t|
      t.string    :path
      t.integer   :user_id
    end
  end
end
