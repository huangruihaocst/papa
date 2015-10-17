
namespace :wc do
  task :default => [:all]

  task :all => :environment do
    puts `./tools/wc`
  end

  task con: :environment do
    puts `./tools/wc con`
  end

  task test: :environment do
    puts `./tools/wc test`
  end

  task view: :environment do
    puts `./tools/wc view`
  end

end