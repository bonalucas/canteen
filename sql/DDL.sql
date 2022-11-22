-- 创建数据库用户
-- 使用系统默认表空间USERS以及临时表空间TEMP
CREATE USER root IDENTIFIED BY "123456"
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP;

-- 授权
GRANT CONNECT, RESOURCE to root;
grant UNLIMITED TABLESPACE to root;

-- 用户表
CREATE TABLE root.myUser (
     userId		NUMBER(19),
     name		VARCHAR2(25)	NOT NULL,
     username	VARCHAR2(25) 	NOT NULL,
     password	VARCHAR2(25)	NOT NULL,
     sex		VARCHAR2(25)	NOT NULL,
     telephone	VARCHAR2(25)	NOT NULL,
     department	VARCHAR2(25)	NOT NULL,
     role		VARCHAR2(25)	NOT NULL,
     Constraint PK_MYUSER_USERID primary key(userId),  -- 主键
     Constraint UK_MYUSER_USERNAME unique(username)  -- 唯一
);

-- 订单表
CREATE TABLE root.orderForm (
    orderId		NUMBER(19),
    userId		NUMBER(19)		NOT NULL,
    name		VARCHAR2(25) 	NOT NULL,
    telephone	VARCHAR2(25)	NOT NULL,
    orderTime	DATE			NOT NULL,
    orderPrice	NUMBER(18)		NOT NULL,
    Constraint PK_ORDERFORM_ORDERID primary key(orderId)  -- 主键
);

-- 食谱表
CREATE TABLE root.recipe (
    recipeId	NUMBER(19),
    name		VARCHAR2(25)	NOT NULL,
    category	VARCHAR2(25) 	NOT NULL,
    picture		VARCHAR2(50),
    unit		VARCHAR2(25)	NOT NULL,
    price		NUMBER(18)		NOT NULL,
    description	VARCHAR2(200)	NOT NULL,
    Constraint PK_RECIPE_RECIPEID primary key(recipeId)  -- 主键
);

-- 菜单表
CREATE TABLE root.menu (
    menuId		NUMBER(19),
    name		VARCHAR2(25)	NOT NULL,
    category	VARCHAR2(25) 	NOT NULL,
    picture		VARCHAR2(50),
    unit		VARCHAR2(25)	NOT NULL,
    price		NUMBER(18)		NOT NULL,
    createTime	DATE			NOT NULL,
    Constraint PK_MENU_MENUID primary key(menuId)  -- 主键
);

-- 历史菜单表
CREATE TABLE root.history (
    hisId		    NUMBER(19),
    timeRange       VARCHAR(50)     NOT NULL,
    menuIds         VARCHAR2(500)   NOT NULL,
    Constraint PK_SC_HISID primary key(hisId)  -- 主键
);

-- 总括订单表
CREATE TABLE root.blanketOrder (
    mealId		NUMBER(19),
    name		VARCHAR2(25)	NOT NULL,
    unit		VARCHAR2(25) 	NOT NULL,
    weight		NUMBER(8)		NOT NULL,
    price		NUMBER(18)		NOT NULL,
    totalPrice	NUMBER(18)		NOT NULL,
    orderId		NUMBER(19)		NOT NULL,
    createTime	DATE			NOT NULL,
    Constraint PK_BO_MEALID primary key(mealId)  -- 主键
);

-- 购物车表
CREATE TABLE root.shopCart (
    scId		NUMBER(19),
    name		VARCHAR2(25)	NOT NULL,
    unit		VARCHAR2(25) 	NOT NULL,
    weight		NUMBER(8)		NOT NULL,
    price		NUMBER(18)		NOT NULL,
    totalPrice	NUMBER(18)		NOT NULL,
    picture		VARCHAR2(50),
    userId		NUMBER(19)		NOT NULL,
    Constraint PK_SC_SCID primary key(scId)  -- 主键
);

-- 销售统计表
CREATE TABLE root.sale (
    saleId		NUMBER(19),
    month		VARCHAR2(25)	NOT NULL,
    totalPrice	NUMBER(18)		NOT NULL,
    Constraint PK_SC_SALEID primary key(saleId)  -- 主键
);

INSERT INTO root.MyUser VALUES (1397849739276890114, 'manager', 'manager', '123456', '男', '15712131178', '财务部', 'manager');


-- 分类：菜肴、甜点、主食、饮品
-- 计量单位：份、两、个、杯
-- 部门：财务部、生产部、技术部、营销部
-- 角色：餐厅经理(manager)、厨房大厨(chef)、配餐员(caterer)、财务管理(treasurer)、企业员工(staff)