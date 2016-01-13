module ApplicationHelper

  module TokenHelpers

  end

  module StatusRenderingHelpers

    InvalidFieldException = Class.new(Exception)

    def json_failed(reason = nil)
      addition = {}
      if block_given?
        yield addition
      end

      if reason
        render json: { status: 'failed', reason: reason ? reason : REASON_INTERNAL_ERROR }.merge(addition)
      end
    end

    def json_failed_invalid_fields(keys, fields = {})
      json_failed(REASON_INVALID_FIELD) do |json|
        json[INVALID_FIELDS_NAME] = keys.map do |x|
          if fields.keys.include?(x)
            fields[x].to_s
          else
            x.to_s
          end
        end.compact
      end
    end

    def json_successful(parameters = nil)
      addition = parameters ? parameters.clone : {}
      if block_given?
        yield addition
      end

      render json: { status: 'successful' }.merge(addition)
    end

  end

end
