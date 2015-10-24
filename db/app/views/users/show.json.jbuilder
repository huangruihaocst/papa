json.status STATUS_SUCCESS
json.user do
  json.id         @user.id
  json.name       @user.name
  json.email      @user.email
  json.phone      @user.phone
  json.is_teacher @user.is_teacher
  json.is_admin   @user.is_admin
end