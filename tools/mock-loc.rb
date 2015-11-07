require 'optparse'
require 'timeout'
require 'net/telnet'

# seconds
ANALOG_INTERVAL = 1

CENTER_POSTION = [ 0, 0 ]

# default values
options = {
  mode: :help,
  velocity: 1,
  precision: 3,
  interval: ANALOG_INTERVAL
}

# internal functions
internal_functions = {
  circle_by_equator: ['(v*t+180)%360-180', '0'],
  circle_by_lat: ['0', '(v*t+90)%180-90']
}


OptionParser.new do |opts|
  opts.banner = "Usage: mock-loc.rb [options]"

  opts.on('-f', '--function LONG,LAT', Array, 
          'Mock location using math functions, t(s)') do |list|
    options[:mode] = :analog
    options[:long] = list[0]
    options[:lat]= list[1]
  end

  opts.on('-h', '--help', 'Show help messages.') do |v|
    puts opts
  end

  opts.on('-i', '--internal-functions FUNCTION', 'use internal functions to analog') do |func|
    options[:mode] = :analog
    f = internal_functions[func.to_sym]
    options[:long] = f[0]
    options[:lat] = f[1]
  end

  opts.on('-p', '--position LONG LAT', 'fix location at LONG LAT') do |long, lat|
    options[:mode] = :fix
    options[:long] = long
    options[:lat] = lat
  end
  
  opts.on('-r', '--precision N', Integer, 'set float number precision') do |prec|
    options[:precision] = prec
  end

  opts.on('-t', '--interval INTERVAL', Float, 'analog interval') do |interval|
    options[:interval] = interval
  end

  opts.on('-v', '--velocity V', Float, 'set velocity(degree/s)') do |v|
    options[:velocity] = v
  end

end.parse!

p options
p ARGV

telnet = Net::Telnet.new(
  'Host'  =>  'localhost',
  'Port'  =>  '5554',
  'Prompt'=>  /OK/n
)

case options[:mode]
  when :fix
    long, lat = options[:long], options[:lat]
    telnet.cmd("geo fix #{long} #{lat}") 
  when :analog
    t = 0
    loop do
      v = options[:velocity]
      long, lat = eval(options[:long]), eval(options[:lat])  
      telnet.cmd("geo fix #{long} #{lat}")
      puts "time: #{t.round(options[:precision])}, 
        long: #{long.round(options[:precision])}, 
        lat: #{lat.round(options[:precision])}"
      t = t + options[:interval]
      sleep options[:interval]
    end 
end

telnet.close
