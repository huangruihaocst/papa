class MessageBuilder

  HOMEWORK = %w'实验报告 书面作业 开题报告 中期报告 终期报告 毕业论文 演讲稿'
  NOTIFICATIONS = %w'交作业提醒 期中考试 期末考试 大作业截止提醒 演讲提醒 课程停上 假期串休'

  def initialize

  end

  def self.build_homework
    HOMEWORK[Random.rand(HOMEWORK.size)]
  end

  def self.build_notification
    NOTIFICATIONS[Random.rand(NOTIFICATIONS.size)]
  end

  def self.build_time
    Time.now + Random.rand(100) * 1.hours
  end

end