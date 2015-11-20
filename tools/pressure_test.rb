#!/usr/bin/env ruby
require 'json'
require 'tick-tock'
require 'optparse'

host = 'localhost'
port = '80'
OptionParser.new do |opts|
  opts.banner = "PressureTest!"
  
  opts.on('-h', '--host HOST', 'Host address') do |v|
    host = v 
  end
  opts.on('-p', '--port PORT', 'Port') do |v|
    port = v
  end
  opts.on('-v', '--verbose', 'Verbose Mode') do |v|
    verbose = true
  end
end.parse!
puts "host: #{host}, port: #{port}"

REQUEST_COUNT_PER_LOOP = 100

# initialize with token and user id
verbose = false
response = `curl -s "http://#{host}:#{port}/users/sign_in.json" -F "user[login]=41" -F "user[password]=123"`
json = JSON.parse(response)
id = json['id']
token = json['token']

prefix = "http://#{host}:#{port}"
REQUEST_POOL = [
  "#{prefix}/semesters.json?",
  "#{prefix}/semesters/1/courses.json?",
  "#{prefix}/courses/1.json"
]

flow = 0

time = TickTock.time do 
  REQUEST_COUNT_PER_LOOP.times do
    url = "#{REQUEST_POOL.sample}&token=#{token}"
    puts "URL: #{url}" if verbose
    response = `curl -s #{url}`
    puts "response: #{response}" if verbose
    flow += response.size
  end
end

millis = time.raw_millis
puts "Total request: #{REQUEST_COUNT_PER_LOOP}"
puts "Total time elapse: #{millis}ms"
puts "#{(REQUEST_COUNT_PER_LOOP / (millis/1000.0)).round(3)} requests/s"
puts "#{(flow/1000.0/(millis/1000.0)).round(3)} KB/s"
puts "#{(flow/1000.0/REQUEST_COUNT_PER_LOOP).round(3)} KB/request"
