module ApplicationHelper

  module TokenHelpers

  end

  module StatusRenderingHelpers

    class InvalidFieldException < Exception
    end

    def json_failed(reason = nil)
      addition = {}
      if block_given?
        yield(addition)
      end

      if reason
        render json: { status: 'failed', reason: reason }.merge(addition)
      else
        render json: { status: 'failed' }.merge(addition)
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

    def json_successful
      render json: { status: 'successful' }
    end

    def html_failed(reason = nil)
      redirect_to '/404.html'
    end

  end

end
