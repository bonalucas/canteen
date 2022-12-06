-------------------------------------
-- INIT.sql
-- 初始化用户表信息、食谱信息
-------------------------------------

-- 用户表初始化
insert into MYUSER (userid, name, username, password, sex, telephone, department, role)
values (1588202087768961025, '员工', 'staff2', '123456', '男', '15789653205', '技术部', 'staff');
insert into MYUSER (userid, name, username, password, sex, telephone, department, role)
values (1595067017944141825, 'staff3', 'staff3', '123456', '男', '12345678911', '技术部', 'staff');
insert into MYUSER (userid, name, username, password, sex, telephone, department, role)
values (1580717067441590273, 'chef', 'chef', '123456', '男', '12501336501', '生产部', 'chef');
insert into MYUSER (userid, name, username, password, sex, telephone, department, role)
values (1580717246668394498, 'caterer', 'caterer', '123456', '女', '14523532178', '生产部', 'caterer');
insert into MYUSER (userid, name, username, password, sex, telephone, department, role)
values (1580717341635825665, 'treasurer', 'treasurer', '123456', '女', '16253401289', '财务部', 'treasurer');
insert into MYUSER (userid, name, username, password, sex, telephone, department, role)
values (1580717435110084610, 'staff', 'staff', '123456', '女', '12301572301', '营销部', 'staff');
insert into MYUSER (userid, name, username, password, sex, telephone, department, role)
values (1595067112211124225, 'staff4', 'staff4', '123456', '女', '12345678999', '生产部', 'staff');
insert into MYUSER (userid, name, username, password, sex, telephone, department, role)
values (1397849739276890114, 'manager', 'manager', '123456', '男', '15712131178', '财务部', 'manager');

-- 食谱表初始化
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1586742596993060865, '烤鸭', '菜肴', 'eae0bf1a-48e2-4d97-929c-688ed4fa1c9a.jpeg', '份', 66, '烤鸭');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1582032939984637953, '鱼香肉丝', '菜肴', '9912c6b3-f8dd-4933-8e43-37c5de85392d.jpg', '份', 47, '味道：甜辣' || chr(10) || '食材：肉丝、....');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1582030292502228993, '麻辣兔头', '菜肴', 'd3495803-e1a5-40c2-bb83-904b4034dd84.jpg', '份', 53, '味道：麻辣' || chr(10) || '食材：辣椒、兔头');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585297767641706498, '北冰洋', '饮品', '83a48e56-53a1-40af-873a-b095ee1cbad9.png', '杯', 3, '北冰洋，冰的，解渴');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585298235453403138, '烤乳鸽', '菜肴', '8f62c8bd-8bb7-49ff-95d6-097bbb8a376c.jpeg', '份', 88, '脆皮烤乳鸽');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585298352906498049, '王老吉', '饮品', '195c3c20-45d4-44ff-9308-57a89d9fec97.png', '杯', 5, '王老吉凉茶');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585298610474512386, '上汤焗龙虾', '菜肴', '705f71c1-a45c-45da-8181-070c5c455785.jpeg', '份', 199, '上汤焗龙虾');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585298746118303745, '东坡肉', '菜肴', 'e8a943c1-4534-47bc-a5bf-a198048e68c4.jpg', '份', 89, '糯糯的东坡肉');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585298953925095426, '蘑菇汤', '饮品', '726c1de4-03b9-4340-9593-0d58683df9a1.jpeg', '份', 128, '清淡蘑菇汤');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585299248528814081, '血肠', '甜点', '12c0fac3-6be7-4453-b540-b5083956f7da.jpg', '两', 35, '血肠血肠血肠');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585300202267406338, '白切鸡', '菜肴', '7a481644-cb81-4ec4-894b-376297ba57a4.jpeg', '份', 68, '半只白切鸡');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1585300388796493826, '甜品吧', '甜点', '7c51fb88-2c15-46ee-9748-f8973a041107.jpg', '个', 12, '应该是甜品吧');
insert into RECIPE (recipeid, name, category, picture, unit, price, description)
values (1582634905643323394, '米饭', '主食', '3e79736f-c299-49a6-8870-e945a584611e.png', '份', 3, '就米饭呗');

