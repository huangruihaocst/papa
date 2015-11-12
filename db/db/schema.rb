# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20151106135008) do

  create_table "android_apps", force: :cascade do |t|
    t.string   "version"
    t.integer  "file_resource_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "assistant_files", force: :cascade do |t|
    t.integer "assistant_id"
    t.integer "lesson_id"
    t.integer "file_resource_id"
  end

  create_table "course_files", force: :cascade do |t|
    t.integer "course_id"
    t.integer "file_resource_id"
  end

  create_table "courses", force: :cascade do |t|
    t.string   "name",        null: false
    t.text     "description"
    t.integer  "semester_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "file_resources", force: :cascade do |t|
    t.string   "file_type"
    t.string   "name"
    t.string   "path"
    t.integer  "creator_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "lesson_comments", force: :cascade do |t|
    t.text     "content"
    t.string   "score"
    t.integer  "creator_id"
    t.integer  "lesson_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "lesson_files", force: :cascade do |t|
    t.integer "lesson_id"
    t.integer "file_resource_id"
  end

  create_table "lesson_statuses", force: :cascade do |t|
    t.string   "score"
    t.integer  "user_id"
    t.integer  "lesson_id"
    t.integer  "creator_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "lessons", force: :cascade do |t|
    t.string   "name",        null: false
    t.text     "description"
    t.datetime "start_time"
    t.datetime "end_time"
    t.string   "location"
    t.integer  "course_id",   null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "messages", force: :cascade do |t|
    t.string   "message_type"
    t.string   "title"
    t.datetime "deadline"
    t.text     "content"
    t.integer  "creator_id"
    t.integer  "course_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "participations", force: :cascade do |t|
    t.integer "course_id"
    t.integer "user_id"
    t.string  "role",      default: "student"
  end

  create_table "semesters", force: :cascade do |t|
    t.string "name"
  end

  create_table "student_attendences", force: :cascade do |t|
    t.integer  "user_id"
    t.integer  "lesson_id"
    t.string   "sign_up_method"
    t.text     "description"
    t.datetime "created_at",     null: false
    t.datetime "updated_at",     null: false
  end

  create_table "student_comments", force: :cascade do |t|
    t.text     "content"
    t.string   "score"
    t.integer  "creator_id"
    t.integer  "lesson_id"
    t.integer  "student_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "student_files", force: :cascade do |t|
    t.integer "student_id"
    t.integer "lesson_id"
    t.integer "file_resource_id"
    t.integer "creator_id"
  end

  create_table "teaching_courses", force: :cascade do |t|
    t.integer "user_id"
    t.integer "course_id"
  end

  create_table "tokens", force: :cascade do |t|
    t.string   "token",       null: false
    t.integer  "user_id",     null: false
    t.datetime "valid_until", null: false
  end

  create_table "user_messages", force: :cascade do |t|
    t.integer  "sender_id"
    t.integer  "receiver_id"
    t.string   "title"
    t.text     "content"
    t.string   "status"
    t.boolean  "sender_deleted",   default: false, null: false
    t.boolean  "receiver_deleted", default: false, null: false
    t.datetime "created_at",                       null: false
    t.datetime "updated_at",                       null: false
  end

  create_table "users", force: :cascade do |t|
    t.string   "name",                   default: ""
    t.string   "phone"
    t.string   "email",                  default: "",    null: false
    t.string   "encrypted_password",     default: "",    null: false
    t.string   "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          default: 0,     null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip"
    t.string   "last_sign_in_ip"
    t.boolean  "is_teacher",             default: false
    t.boolean  "is_admin",               default: false
    t.integer  "avator_id"
    t.string   "student_number",         default: "",    null: false
    t.string   "class_name",             default: "",    null: false
    t.string   "department",             default: "",    null: false
    t.text     "description",            default: "",    null: false
    t.datetime "created_at",                             null: false
    t.datetime "updated_at",                             null: false
  end

  add_index "users", ["email"], name: "index_users_on_email", unique: true
  add_index "users", ["phone"], name: "index_users_on_phone", unique: true
  add_index "users", ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true

end
