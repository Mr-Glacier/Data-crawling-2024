CREATE TABLE T_DiYiCarSimpleInfo
(
    C_ID             INT AUTO_INCREMENT PRIMARY KEY,
    C_wareHouseTime  VARCHAR(200),
    C_carName        VARCHAR(200),
    C_sourceId       VARCHAR(200),
    C_sourceFileName VARCHAR(200),
    C_licenseDate    VARCHAR(200),
    C_mileage        VARCHAR(200),
    C_image          VARCHAR(200),
    C_tags           VARCHAR(200),
    C_price          VARCHAR(200),
    C_detailUrl      VARCHAR(200),
    C_downState      VARCHAR(200),
    C_downTime       VARCHAR(200)
);
# 再创建一个表
CREATE TABLE T_DiYiCarDetailInfo
(
    C_ID                      INT AUTO_INCREMENT PRIMARY KEY,
    C_wareHouseTime           VARCHAR(200),
    C_carName                 VARCHAR(200),
    C_sourceId                VARCHAR(200),
    C_sourceFileName          VARCHAR(200),
    C_licenseDate             VARCHAR(200),
    C_mileage                 VARCHAR(200),
    C_image                   VARCHAR(200),
    C_tags                    VARCHAR(200),
    C_price                   VARCHAR(200),
    C_detailUrl               VARCHAR(200),
    C_detailName              VARCHAR(200),
    C_newCarPriceIncludingTax VARCHAR(200),
    C_newCarPrice             VARCHAR(200),
    C_vehiclePurchaseTax      VARCHAR(200),
    C_downPayment             VARCHAR(200),
    C_monthlyPayment          VARCHAR(200),
    C_detailEmission          VARCHAR(200),
    C_detailDisplacement      VARCHAR(200),
    C_location                VARCHAR(200)
);



