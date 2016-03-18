class NameReader
  def initialize
    f = open('db/init_data/names', 'r')
    name_str = f.read
    f.close
    @names = name_str.split(' ')
  end

  def read
    index = Random.rand(@names.size)
    name = @names[index]
    @names.delete_at(index)
    name
  end
end