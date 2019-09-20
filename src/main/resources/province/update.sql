update t_area set areaLevel = 2 where parentId !=1

create TABLE t_area_bak select * from t_area where areaLevel = 1 || (areaLevel = 2 and parentId in (select id from t_area where areaLevel = 1))