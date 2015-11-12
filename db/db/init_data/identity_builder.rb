class IdentityBuilder
  DEPARTMENTS = %w"计算机 电子 自动化 软件工程 数学 物理 新闻 美术 电机 外文系"
  CLASSES     = %w"计     无   自     软件    数   物   新   美   电  外"

  def self.build_department_and_class
    index = Random.rand(DEPARTMENTS.size)
    { department: DEPARTMENTS[index],
      class:      CLASSES[index] + Random.rand(10).to_s + Random.rand(1..10).to_s }
  end

end