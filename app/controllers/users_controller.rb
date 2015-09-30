class UsersController < ApplicationController
  def api_index
    ids = []
    User.all.each do |user|
      ids.push(user.id)
    end
    render json: {
               status: 'success',
               ids: ids
           }
  end

  def api_show
    begin
      user = User.find(params[:id])
      render json: {
                 status: 'success',
                 result: {
                     id:          user.id,
                     name:        user.name,
                 }
             }
    rescue ActiveRecord::RecordNotFound
      render json: {
                 status: 'not-found'
             }
    end
  end

  def api_find
    begin
      user = User.find_by_name(params[:name])
      render json: {
                 status: 'success',
                 result: {
                     id:          user.id,
                     name:        user.name,
                 }
             }
    rescue ActiveRecord::RecordNotFound
      render json: {
                 status: 'not-found'
             }
    end
  end

end