class LocationBuilder
  def self.build
    pos = Random.rand(6)
    num_str = %w'一 二 三 四 五 六'[pos]
    "#{num_str}教-#{pos+1}#{Random.rand(100..399)}"
  end
end