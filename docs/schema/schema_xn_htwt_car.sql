
/*
 * 平台用户（菜单、角色）系统参数（数据字典、系统参数） tsys_ 柴
 * C端用户+账户 tstd_  郑
 * 商品分期 tmall_
 * 车贷意向 车贷分期 还款业务 tht_
 * 
 * */

/*意向收集*/
CREATE TABLE `tht_brand` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `letter` varchar(32) DEFAULT NULL COMMENT '字母序号',
  `logo` varchar(255) DEFAULT NULL COMMENT 'logo',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `description` text COMMENT '品牌介绍',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `updater` varchar(32) DEFAULT NULL COMMENT '最新修改人',
  `update_datetime` datetime DEFAULT NULL COMMENT '最新修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`code`) COMMENT '品牌'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tht_series` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `brand_code` varchar(32) DEFAULT NULL COMMENT '品牌编号',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `slogan` text COMMENT '广告语',
  `adv_pic` varchar(255) DEFAULT NULL COMMENT '广告图',
  `price` bigint(20) DEFAULT NULL COMMENT '价格区间',
  `location` int(11) DEFAULT NULL COMMENT 'UI位置',
  `order_no` int(11) DEFAULT NULL COMMENT 'UI次序',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `updater` varchar(32) DEFAULT NULL COMMENT '最新修改人',
  `update_datetime` datetime DEFAULT NULL COMMENT '最新修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`code`) COMMENT '车系'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tht_car` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `series_code` varchar(32) DEFAULT NULL COMMENT '车系编号',
  `series_name` varchar(255) DEFAULT NULL COMMENT '车系名称',
  `brand_code` varchar(32) DEFAULT NULL COMMENT '品牌编号',
  `brand_name` varchar(255) DEFAULT NULL COMMENT '品牌名称',
  `original_price` bigint(32) DEFAULT NULL COMMENT '原价',
  `sale_price` bigint(32) DEFAULT NULL COMMENT '参考价',
  `sf_amount` bigint(20) DEFAULT NULL COMMENT '首付金额',
  `location` int(11) DEFAULT NULL COMMENT 'UI位置',
  `order_no` int(11) DEFAULT NULL COMMENT 'UI次序',
  `slogan` text COMMENT '广告语',
  `adv_pic` varchar(255) DEFAULT NULL COMMENT '广告图',
  `pic` varchar(255) DEFAULT NULL COMMENT '缩略图',
  `description` text COMMENT '图文描述',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `updater` varchar(32) DEFAULT NULL COMMENT '最新修改人',
  `update_datetime` datetime DEFAULT NULL COMMENT '最新修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`code`) COMMENT '车型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tht_car_order` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `user_id` varchar(32) DEFAULT NULL COMMENT '申请人编号',
  `user_mobile` varchar(16) DEFAULT NULL COMMENT '申请人手机号',
  `brand_code` varchar(32) DEFAULT NULL COMMENT '品牌编号',
  `brand_name` varchar(255) DEFAULT NULL COMMENT '品牌名称',
  `series_code` varchar(32) DEFAULT NULL COMMENT '车系编号',
  `series_name` varchar(255) DEFAULT NULL COMMENT '车系名称',
  `car_code` varchar(32) DEFAULT NULL COMMENT '车型编号',
  `car_name` varchar(255) DEFAULT NULL COMMENT '车型名称',
  `price` bigint(20) DEFAULT NULL COMMENT '车辆总价',
  `sf_rate` decimal(18,8) DEFAULT NULL COMMENT '首付比例',
  `sf_amount` bigint(20) DEFAULT NULL COMMENT '首付金额',
  `periods` int(11) DEFAULT NULL COMMENT '分期期数',
  `create_datetime` datetime DEFAULT NULL COMMENT '申请时间',
  `sale_desc` text COMMENT '计算器信息',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `handler` varchar(32) DEFAULT NULL COMMENT '处理人',
  `handle_datetime` datetime DEFAULT NULL COMMENT '处理时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`code`) COMMENT '购车意向'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*车贷分期*/
CREATE TABLE `tht_loan_order` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `repay_biz_code` varchar(32) DEFAULT NULL COMMENT '还款业务编号',
  `user_id` varchar(32) DEFAULT NULL COMMENT '申请人编号',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号',
  `id_kind` varchar(4) DEFAULT NULL COMMENT '证件类型',
  `id_no` varchar(64) DEFAULT NULL COMMENT '证件号',
  `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `bank_code` varchar(32) DEFAULT NULL COMMENT '银行行别',
  `bank_name` varchar(255) DEFAULT NULL COMMENT '银行名称',
  `subbranch` varchar(255) DEFAULT NULL COMMENT '开户支行',
  `bankcard_number` varchar(64) DEFAULT NULL COMMENT '银行卡号',
  `car_code` varchar(32) DEFAULT NULL COMMENT '车型编号',
  `car_price` bigint(20) DEFAULT NULL COMMENT '车辆总价',
  `sf_rate` decimal(18,8) DEFAULT NULL COMMENT '首付比例',
  `sf_amount` bigint(20) DEFAULT NULL COMMENT '首付金额',
  `loan_bank` varchar(32) DEFAULT NULL COMMENT '贷款银行',
  `loan_amount` bigint(20) DEFAULT NULL COMMENT '贷款金额',
  `periods` int(11) DEFAULT NULL COMMENT '总期数',
  `bank_rate` decimal(18,8) DEFAULT NULL COMMENT '银行利率',
  `loan_start_datetime` datetime DEFAULT NULL COMMENT '贷款时间起点',
  `loan_end_datetime` datetime DEFAULT NULL COMMENT '贷款时间终点',
  `fk_datetime` datetime DEFAULT NULL COMMENT '放款日期',
  `fx_deposit` bigint(20) DEFAULT NULL COMMENT '风险保证金',
  `other_fee` bigint(20) DEFAULT NULL COMMENT '杂费',
  `gps_fee` bigint(20) DEFAULT NULL COMMENT 'GPS收费',
  `first_repay_datetime` datetime DEFAULT NULL COMMENT '首期还款日期',
  `first_repay_amount` bigint(20) DEFAULT NULL COMMENT '首期月供金额',
  `month_datetime` int(11) DEFAULT NULL COMMENT '每期还款日期',
  `month_amount` bigint(20) DEFAULT NULL COMMENT '每期月供金额',
  `ly_deposit` bigint(20) DEFAULT NULL COMMENT '履约保证金',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `updater` varchar(32) DEFAULT NULL COMMENT '最近修改人',
  `update_datetime` datetime DEFAULT NULL COMMENT '最近修改时间',
  `remark` varchar(32) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*还款业务 */
CREATE TABLE `tht_repay_biz` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `user_id` varchar(32) DEFAULT NULL COMMENT '申请人编号',
  `bankcard_code` varchar(32) DEFAULT NULL COMMENT '还款卡编号',
  `ref_type` varchar(4) DEFAULT NULL COMMENT '关联类型',
  `ref_code` varchar(32) DEFAULT NULL COMMENT '关联编号',
  `biz_price` bigint(20) DEFAULT NULL COMMENT '业务总价',
  `sf_rate` decimal(18,8) DEFAULT NULL COMMENT '首付比例',
  `sf_amount` varchar(20) DEFAULT NULL COMMENT '首付金额',
  `loan_bank` varchar(32) DEFAULT NULL COMMENT '贷款银行',
  `loan_amount` bigint(20) DEFAULT NULL COMMENT '贷款金额',
  `periods` int(11) DEFAULT NULL COMMENT '总期数',
  `rest_periods` int(11) DEFAULT NULL COMMENT '剩余期数',
  `bank_rate` decimal(18,8) DEFAULT NULL COMMENT '银行利率',
  `loan_start_datetime` datetime DEFAULT NULL COMMENT '贷款时间起点',
  `loan_end_datetime` datetime DEFAULT NULL COMMENT '贷款时间终点',
  `fx_deposit` bigint(20) DEFAULT NULL COMMENT '风险保证金',
  `first_repay_datetime` datetime DEFAULT NULL COMMENT '首期还款日期',
  `first_repay_amount` bigint(20) DEFAULT NULL COMMENT '首期月供金额',
  `month_datetime` int(11) DEFAULT NULL COMMENT '每期还款日期',
  `month_amount` bigint(20) DEFAULT NULL COMMENT '每期月供金额',
  `ly_deposit` bigint(20) DEFAULT NULL COMMENT '履约保证金（可退）',
  `cut_ly_deposit` bigint(20) DEFAULT NULL COMMENT '扣除的履约保证金',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `rest_amount` bigint(20) DEFAULT NULL COMMENT '剩余欠款',
  `rest_total_cost` bigint(20) DEFAULT NULL COMMENT '未还清收总成本',
  `total_in_deposit` bigint(20) DEFAULT NULL COMMENT '额外保证金收入',
  `overdue_amount` bigint(20) DEFAULT NULL COMMENT '逾期总金额',
  `total_overdue_count` int(11) DEFAULT NULL COMMENT '累计逾期期数',
  `cur_overdue_count` int(11) DEFAULT NULL COMMENT '实际逾期期数',
  `black_handle_note` text COMMENT '黑名单处理结果备案',
  `close_attach` varchar(255) DEFAULT NULL COMMENT '结清证明',
  `close_datetime` datetime DEFAULT NULL COMMENT '结清时间',
  `updater` varchar(255) DEFAULT NULL COMMENT '最近修改人',
  `update_datetime` datetime DEFAULT NULL COMMENT '最近修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*还款计划 */
CREATE TABLE `tht_repay_plan` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `repay_biz_code` varchar(32) DEFAULT NULL COMMENT '还款业务编号',
  `user_id` varchar(32) DEFAULT NULL COMMENT '借款人编号',
  `periods` int(11) DEFAULT NULL COMMENT '总期数',
  `cur_periods` int(11) DEFAULT NULL COMMENT '当前期数',
  `repay_datetime` datetime DEFAULT NULL COMMENT '还款时间',
  `repay_capital` bigint(20) DEFAULT NULL COMMENT '本期本金',
  `repay_interest` decimal(18,8) DEFAULT NULL COMMENT '本期利息',
  `payed_amount` bigint(20) DEFAULT NULL COMMENT '已还金额',
  `overdue_amount` bigint(20) DEFAULT NULL COMMENT '逾期金额',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `overdue_handler` varchar(255) DEFAULT NULL COMMENT '逾期处理人',
  `overdue_handle_datetime` datetime DEFAULT NULL COMMENT '逾期处理时间',
  `overdue_handle_note` text COMMENT '逾期处理说明',
  `total_fee` bigint(20) DEFAULT NULL COMMENT '清收费用总额',
  `payed_fee` bigint(20) DEFAULT NULL COMMENT '已缴纳清收费用总额',
  `overdue_deposit` bigint(20) DEFAULT NULL COMMENT '再次逾期保证金',
  `deposit_way` varchar(255) DEFAULT NULL COMMENT '再次逾期保证金收取方式',
  `should_deposit` bigint(20) DEFAULT NULL COMMENT '实际可退的再次逾期保证金',
  `remind_count` int(11) DEFAULT NULL COMMENT '已催款次数',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tht_cost` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `repay_plan_code` varchar(32) DEFAULT NULL COMMENT '还款计划编号',
  `item` varchar(255) DEFAULT NULL COMMENT '费用项名称',
  `amount` bigint(20) DEFAULT NULL COMMENT '金额',
  `pay_datetime` datetime DEFAULT NULL COMMENT '发生时间',
  `pay_way` varchar(255) DEFAULT NULL COMMENT '发生付款方式',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `repay_datetime` datetime DEFAULT NULL COMMENT '执行时间',
  `repay_way` varchar(255) DEFAULT NULL COMMENT '执行付款方式',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*催收记录 */
CREATE TABLE `tht_remind_log` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `repay_plan_code` varchar(32) DEFAULT NULL COMMENT '还款计划编号',
  `way` varchar(255) DEFAULT NULL COMMENT '催收方式',
  `to_user` varchar(255) DEFAULT NULL COMMENT '催收对象姓名',
  `content` text COMMENT '催收文本',
  `create_datetime` datetime DEFAULT NULL COMMENT '催收时间',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;










