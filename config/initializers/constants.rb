
ROLE_STUDENT = 'student'
ROLE_ASSISTANT = 'assistant'
STATUS_SUCCESS = 'successful'
STATUS_FAIL = 'failed'

TOKEN_MAX_RAND = 2**192 - 1
TOKEN_VALID_TIME_SEC = 2 * 60 * 60 # 2h
FILE_MAX_SIZE = 100.megabytes

REASON_PERMISSION_DENIED = 'permission_denied'
REASON_TOKEN_INVALID = 'token_invalid'
REASON_TOKEN_TIMEOUT = 'token_timeout'
REASON_TOKEN_NOT_MATCH = 'token_not_match'
REASON_NOT_IMPLEMENTED = 'not_implemented'
REASON_INVALID_OPERATION = 'invalid_operation'

# json parameters when a user post an invalid json.
REASON_INVALID_FIELD = 'invalid_field'
INVALID_FIELDS_NAME = 'invalid_fields'
REASON_FILE_TOO_BIG = 'file_too_big'

ALLOWED_FILE_SUFFIXES = [ 'jpg', 'gif', 'png', 'mp4', 'mpg', 'avi' ]