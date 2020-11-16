# 加密芯片型号表添加国家备案编码字段 2019年8月2日 19:45:32  周贤舟
ALTER TABLE sys_encryption_chip_model ADD filing_code VARCHAR(12) COMMENT '国家备案编码';
