/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


INSERT INTO tbl_version (id, batch_id, reason, version, batch_name, created_date, run_status)
VALUES (1, 1, NULL, 1, 'whitelist_batch', '2020-06-02 20:04:37', 'SUCCESS'),
       (2, 1, NULL, 2, 'WhiteList batch', '2020-06-17 00:00:00', 'SUCCESS'),
       (3, 1, NULL, 3, 'whitelist_batch', '2020-06-02 20:04:37', 'SUCCESS'),
       (4, 1, NULL, 4, 'WhiteList batch', '2020-06-17 00:00:00', 'SUCCESS'),
       (5, 2, NULL, 1, 'Paid Loan', '2020-06-18 00:00:00', 'SUCCESS'),
       (6, 2, NULL, 2, 'Paid Loan', '2020-06-19 00:00:00', 'SUCCESS'),
       (7, 2, NULL, 3, 'Paid Loan', '2020-06-18 00:00:00', 'FAILED'),
       (8, 2, NULL, 4, 'Paid Loan', '2020-06-19 00:00:00', 'FAILED');

INSERT INTO tbl_interest (id, created_date, last_modified_date, description, interest_rate, loan_term, name, status)
VALUES (1, NULL, NULL, 'lãi tháng', '10', '1 tháng', 'lãi tháng', 'hoạt động'),
       (2, NULL, NULL, 'lãi kỳ', '5', '6 tháng', 'lãi kỳ', 'hoạt động'),
       (3, NULL, NULL, 'lãi tháng', '2', '2 tháng', 'lãi tháng', 'hoạt động'),
       (4, NULL, NULL, 'lãi tháng', '0.5', '3 tháng', 'lãi tháng', 'hoạt động'),
       (5, NULL, NULL, 'lãi tháng', '1.5', '4  tháng', 'lãi tháng', 'hoạt động');

INSERT INTO tbl_reference (id, email, full_name, msisdn)
VALUES (1, 'abc@gmail.com', 'HQV', '0368774849'),
       (2, 'zyx@gmail.com', 'meme', '0368774840'),
       (3, 'LMP@gmail.com', 'Nguyen Thi An', '0337499333'),
       (4, 'huong111@gmail.com', 'Nguyen Thi An', '0337499333'),
       (5, 'trangttt@gmail.com', 'TrangTT', '0395954666');

INSERT INTO tbl_code_code (id, code_name, code_type, code_type_name)
VALUES ('KVS_01', 'Chưa vay', '3', 'Trạng thái khoản vay'),
       ('KVS_02', 'Đang vay', '3', 'Trạng thái khoản vay'),
       ('KVS_03', 'Chưa tất toán', '3', 'Trạng thái khoản vay'),
       ('KVS_04', 'Đã tất toán', '3', 'Trạng thái khoản vay'),
       ('KVS_05', 'Từ chối khoản vay', '3', 'Trạng thái khoản vay'),
       ('MQHS_01', 'Anh/ Chị/ Em ruột', '4', 'Mối quan hệ'),
       ('MQHS_02', 'Bạn bè/ Đồng nghiệp sống cùng tỉnh', '4', 'Mối quan hệ'),
       ('MQHS_03', 'Con', '4', 'Mối quan hệ'),
       ('MQHS_04', 'Họ hàng sống cùng tỉnh', '4', 'Mối quan hệ'),
       ('MQHS_05', 'Vợ/ Chồng', '4', 'Mối quan hệ'),
       ('PDS_01', 'Chờ hoàn thiện hồ sơ', '2', 'Trạng thái phê duyệt'),
       ('PDS_02', 'Chờ MB phê duyệt', '2', 'Trạng thái phê duyệt'),
       ('PDS_03', 'Đã phê duyệt', '2', 'Trạng thái phê duyệt'),
       ('PDS_04', 'Từ chối phê duyệt', '2', 'Trạng thái phê duyệt');

INSERT INTO tbl_customer_ref (id, created_date, last_modified_date, customer_id, reference_id, relationship_id)
VALUES (1, '2020-06-17 00:00:00', NULL, 10, 1, 'MQHS_01'),
       (2, '2020-06-17 00:00:00', NULL, 11, 3, 'MQHS_02'),
       (3, '2020-06-17 00:00:00', NULL, 12, 4, 'MQHS_03'),
       (4, '2020-06-17 00:00:00', NULL, 8, 5, 'MQHS_03'),
       (5, '2020-06-23 11:28:47', NULL, 39, 1, 'MQHS_01');

INSERT INTO tbl_address (id, created_date, last_modified_date, address_detail, district, province, village)
VALUES (1, '2020-05-29 00:00:00', NULL, 'Thuy Khue, Tay Ho, Ha Noi', 'Tay Ho ', 'Ha Noi', 'Dong'),
       (2, '2020-05-29 00:00:00', NULL, 'Doi Can, Ba Dinh, Ha Noi', 'Ba Dinh', 'Ha Noi', 'Lang'),
       (3, '2020-05-29 00:00:00', NULL, 'TP Vinh, Tỉnh Nghe An', 'TP Vinh', 'Tỉnh Nghệ An', 'TX Hưng Nguyên'),
       (4, '2020-05-29 00:00:00', NULL, 'Xa Dan, Dong Da, Ha Noi', 'Dong Da', 'Ha Noi', 'Kim Hoa');

INSERT INTO `tbl_customer` (`id`, `created_date`, `last_modified_date`, `arpu`, `arpu_latest_three_months`,
                            `batch_status`, `customer_account`, `date_of_birth`, `date_of_issue`, `email`, `full_name`,
                            `gender`, `identity_number`, `identity_type`, `loan_maximum`, `loan_minimum`, `lock_status`,
                            `msisdn`, `nationality`, `place_of_issue`, `score_max`, `score_min`,
                            `version_id`, `viettelpay_wallet`, `address_id`)
VALUES (1, '2020-05-29 00:00:00', '2020-06-03 20:15:47', 600, 550.00, 'INSERT', NULL, '1993-08-01', '2007-08-01',
        'vuhq@gmail.com', 'VuHQ', 'MALE', '145542148', 'CMTND', 5000000.00, 70000000.00, NULL, '0356447841', 'VN', 'HY',
        700, 600, 1, 1, '1'),
       (2, '2020-05-29 00:00:00', '2020-06-03 20:15:47', 600, 550.00, 'INSERT', NULL, '1993-08-01', '2007-08-01',
        'Trangtt@gmail.com', 'HuongNT1', 'MALE', '1451542148', 'CMTND', 5000000.00, 70000000.00, 'LOCK', '0356447840',
        'VN', 'HN', 700, 600, 1, 1, '2'),
       (3, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 600, 550.00, 'INSERT', NULL, '1993-08-01', '2007-08-01',
        'abc@gmail.com', 'HuongNT2', 'MALE', '3534545', 'CMTND', 5000000.00, 70000000.00, 'LOCK', '0385431000', 'VN',
        'HN', 700, 600, 1, 1, '3'),
       (4, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 808, 950.00, 'INSERT', NULL, '1998-06-01', '2017-08-01',
        'phuong@gmail.com', 'Phuongg', 'FEMALE', '123456789', 'CMTND', 250000000.00, 32000000.00, NULL, '01637499333',
        'VN', 'HN', 800, 680, 1, 2, '4'),
       (5, '2020-06-15 00:00:00', '2020-06-16 00:00:00', 325, 510.00, 'INSERT', NULL, '1998-05-31', '2007-08-01',
        'phuong1@gmail.com', 'Phuong 1', 'FEMALE', '096369', 'CMTND', 100000000.00, 4000000.00, NULL, '0968441599',
        'VN', 'HN', 650, 300, 2, 1, '4'),
       (6, '2020-06-15 00:00:00', '2020-06-16 00:00:00', 456, 500.00, 'INSERT', NULL, '1998-05-20', '2007-08-01',
        'phuong2@gmail.com', 'Phuong 2', 'FEMALE', '01325', 'CMTND', 200000000.00, 3600000.00, NULL, '09683575454',
        'VN', 'HN', 355, 310, 2, 3, '3'),
       (7, '2020-06-15 00:00:00', '2020-06-16 00:00:00', 500, 800.00, 'INSERT', NULL, '1968-05-20', '2007-08-01',
        'phuong3@gmail.com', 'Phuong 3', 'MALE', '01325258', 'CMTND', 2000000.00, 1000000.00, NULL, '0123234434', 'VN',
        'HN', 650, 540, 4, 3, '2'),
       (8, '2020-06-15 00:00:00', '2020-06-16 00:00:00', 500, 800.00, 'INSERT', NULL, '1995-05-20', '2007-08-01',
        'trangtt2@gmail.com', 'Trang 1', 'MALE', '152217227', 'CMTND', 2000000.00, 1000000.00, NULL, '0395954777', 'VN',
        'TB', 650, 540, 4, 3, '1'),
       (9, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 500, 500.00, 'INSERT', NULL, '1995-05-20', '2007-08-01',
        'huongnt@gmail.com', 'Huong0', 'MALE', '24354545', 'CMTND', 60000000.00, 30000000.00, NULL, '0395954111', 'VN',
        'NA', 650, 540, 4, 3, '1'),
       (10, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 800, 800.00, 'INSERT', NULL, '1995-05-20', '2007-08-01',
        'huongnt@gmail.com', 'Huong1', 'MALE', '3435435', 'CMTND', 60000000.00, 30000000.00, NULL, '0395954222', 'VN',
        'NA', 650, 540, 4, 3, '2'),
       (11, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 500, 800.00, 'INSERT', NULL, '1995-05-20', '2007-08-01',
        'huongnt@gmail.com', 'Huong2', 'MALE', '456656757', 'CMTND', 60000000.00, 30000000.00, NULL, '0395954333', 'VN',
        'NA', 650, 540, 4, 4, '3'),
       (12, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 500, 700.00, 'INSERT', NULL, '1995-05-20', '2007-08-01',
        'huongnt@gmail.com', 'Huong3', 'FEMALE', '5657676', 'CMTND', 60000000.00, 30000000.00, 'LOCK', '0395954444',
        'Viet Nam', 'NA', 650, 540, 4, 4, '4'),
       (13, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 500, 700.00, 'INSERT', NULL, '1995-05-20', '2007-08-01',
                'huongnt@gmail.com', 'Huong3', 'FEMALE', '565767623', 'CMTND', 60000000.00, 30000000.00, 'UNLOCK', '0395954555',
                'Viet Nam', 'NA', 650, 540, 4, 4, '4')
                ,
       (14, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 500, 700.00, 'INSERT', NULL, '1995-05-20', '2007-08-01',
                'huongnt@gmail.com', 'Huong3', 'FEMALE', '565671623', 'CMTND', 60000000.00, 30000000.00, NULL, '0359154555',
                'Viet Nam', 'NA', 150, 240, 4, 4, '4'),
        (15, '2020-06-16 00:00:00', '2020-06-16 00:00:00', 500, 700.00, 'INSERT', NULL, '1995-05-20', '2007-08-01',
                        'huongnt@gmail.com', 'Huong3', 'FEMALE', '5656716256', 'CMTND', 60000000.00, 30000000.00, NULL, '0392999771',
                        'Viet Nam', 'NA', 150, 240, 4, 4, '4');

INSERT INTO tbl_loan (id, created_date, last_modified_date, amount_pay, amount_spent, arpu_latest_three_months,
                      contract_link, expiration_date, fee, is_automatic_payment, limit_remaining, loan_account,
                      loan_amount, maximum_limit, minimum_limit, profit_amount, reason_rejection, repayment_form,
                      approval_status_id, customer_id, customer_ref_id, interest_id, loan_status_id)
VALUES (2, '2020-06-15 00:00:00', NULL, NULL, NULL, 700.00, NULL, NULL, 1000000.00, 1, NULL, NULL, 60000000.00,
        50000000.00, 70000000.00, NULL, 'aaa', 'Thu goc cuoi ky', 'PDS_03', 2, NULL, 1, 'KVS_04'),
       (3, '2020-06-15 00:00:00', NULL, 3600000.00, 5600000.00, 700.00, NULL, '2021-06-28', 1000000.00, 1, 1000000.00,
        NULL, 50000000.00, 30000000.00, 60000000.00, NULL, 'chưa đủ điều kiện', 'Thu goc cuoi ky', 'PDS_04', 3, 2, 2,
        'KVS_05'),
       (4, '2020-06-15 00:00:00', NULL, 69896555.00, 89562222.00, 700.00, NULL, '2020-06-30', 1000000.00, 1, 2000000.00,
        NULL, 50000000.00, 30000000.00, 60000000.00, 2800000.00, 'andnđn', 'Thu goc cuoi ky', 'PDS_03', 4, 2, 1,
        'KVS_02'),
       (5, '2020-06-15 00:00:00', NULL, 20000000.00, 200000.00, 900000.00, NULL, '2020-06-28', 550000.00, 1, 1000000.00,
        '12345678', 36000000.00, 60000000.00, 1200000.00, 100000.00, NULL, 'Thu goc cuoi ky', 'PDS_03', 5, 3, 2, 'KVS_02'),
       (6, '2020-06-15 00:00:00', NULL, 3600000.00, 1000000.00, 900.00, NULL, '2020-06-29', 650000.00, 1, 2000000.00,
        '' ||
         '', 36000000.00, 60000000.00, 1200000.00, 100000.00, NULL, 'Thu goc cuoi ky', 'PDS_03', 6, NULL, 2,
        'KVS_02'),
       (8, '2020-06-15 00:00:00', NULL, 20000000.00, 200000.00, 900000.00, NULL, NULL, 550000.00, 1, 1000000.00, 'ABC',
        36000000.00, 60000000.00, 1200000.00, 100000.00, NULL, 'Thu goc cuoi ky', 'PDS_03', 7, NULL, 1, 'KVS_03'),
       (9, '2020-06-17 00:00:00', NULL, 20000000.00, 200000.00, 900000.00, NULL, NULL, 550000.00, 1, 1000000.00, 'ABC',
        36000000.00, 60000000.00, 1200000.00, 100000.00, 'nợ 10 tỷ', 'Thu goc cuoi ky', 'PDS_04', 6, NULL, 1, 'KVS_05'),
       (10, '2020-06-17 00:00:00', NULL, 20000000.00, 200000.00, 500.00, '1592894978462.pdf', NULL, 550000.00, 1,
        1000000.00, 'ABC', 36000000.00, 60000000.00, 1200000.00, 100000.00, 'ok', 'Thu goc cuoi ky', 'PDS_02', 9, NULL,
        1, 'KVS_01'),
       (11, '2020-06-17 00:00:00', NULL, 20000000.00, 200000.00, 900000.00, NULL, NULL, 550000.00, 1, 1000000.00, 'ABC',
        36000000.00, 60000000.00, 1200000.00, 100000.00, NULL, 'Thu goc cuoi ky', 'PDS_03', 11, 3, 1, 'KVS_04'),
       (12, '2020-06-17 00:00:00', NULL, 20000000.00, 200000.00, 800.00, NULL, NULL, 550000.00, 1, 1000000.00, 'ABC',
        36000000.00, 60000000.00, 1200000.00, 100000.00, NULL, 'Thu goc cuoi ky', 'PDS_03', 10, 3, 1, 'KVS_02'),
       (13, '2020-06-17 00:00:00', NULL, 20000000.00, 200000.00, 900000.00, NULL, NULL, 550000.00, 1, 1000000.00, 'ABC',
        36000000.00, 60000000.00, 1200000.00, 100000.00, NULL, 'Thu goc cuoi ky', 'PDS_03', 6, NULL, 1, 'KVS_02'),
       (14, '2020-06-17 00:00:00', NULL, 20000000.00, 200000.00, 900000.00, NULL, NULL, 550000.00, 1, 1000000.00, 'ABC',
        36000000.00, 60000000.00, 1200000.00, 100000.00, 'đang nợ\n', 'tra tu dong', 'PDS_04', 8, NULL, 1, 'KVS_05'),
       (15, '2020-06-17 00:00:00', NULL, 20000000.00, 200000.00, 700.00, NULL, NULL, 550000.00, 1, 1000000.00, 'ABC',
        36000000.00, 60000000.00, 1200000.00, 100000.00, '23243434', 'tra tu dong', 'PDS_04', 12, 2, 1, 'KVS_05'),
       (20, '2020-06-17 00:00:00', NULL, 15000000.00, 5000000.00, 750000.00, NULL, NULL, 550000.00, 1, 1000000.00,
        'Tungtq2', 36000000.00, 60000000.00, 1200000.00, 100000.00, 'TungTQ2 đã từ chối phê duyệt', 'tra tu dong',
        'PDS_02', 13, 2, 1, 'KVS_05');
