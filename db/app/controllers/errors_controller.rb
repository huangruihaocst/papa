class ErrorsController < ApplicationController
  def not_found
    json_failed(REASON_INVALID_OPERATION)
  end
end