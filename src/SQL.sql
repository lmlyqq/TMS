--yuanlei 2017-06-08
CREATE OR REPLACE VIEW V_BMS_ORDER_HEADER1 AS
SELECT
T.ID,
t.AUDIT_STAT,
t5.name_c as AUDIT_STAT_NAME,
T.ODR_NO,T.CUSTOM_ODR_NO,T.CUSTOMER_ID,T.CUSTOMER_NAME,T.TRANS_SRVC_ID,T.ODR_TYP
,to_char(T.ODR_TIME,'yyyy/mm/dd hh24:mi') as ODR_TIME
,to_char(T.ODR_TIME,'yyyy/mm/dd') as ODR_DATE
,T.LOAD_ID
,T.LOAD_NAME,T.LOAD_AREA_ID,T.LOAD_AREA_NAME,T.LOAD_ADDRESS
,T.LOAD_CONTACT,T.LOAD_TEL,t.pod_delay_days,t.unload_delay_days
,to_char(T.FROM_LOAD_TIME,'yyyy/mm/dd hh24:mi') as FROM_LOAD_TIME
,to_char(T.PRE_LOAD_TIME,'yyyy/mm/dd hh24:mi') as PRE_LOAD_TIME,
T.UNLOAD_ID,T.UNLOAD_NAME,T.UNLOAD_AREA_ID,T.UNLOAD_AREA_NAME,T.UNLOAD_ADDRESS,T.UNLOAD_CONTACT,T.UNLOAD_TEL
,to_char(T.FROM_UNLOAD_TIME,'yyyy/mm/dd hh24:mi') as FROM_UNLOAD_TIME
,to_char(T.LOAD_TIME,'yyyy/mm/dd hh24:mi') as LOAD_TIME
,to_char(T.OP_POD_TIME,'yyyy/mm/dd hh24:mi') as OP_POD_TIME
,to_char(T.PRE_UNLOAD_TIME,'yyyy/mm/dd hh24:mi') as PRE_UNLOAD_TIME
,to_char(T.PRE_POD_TIME,'yyyy/mm/dd hh24:mi') as PRE_POD_TIME,T.STATUS,T.BILL_TO,T.PRE_ORDER_NO,T.REFENENCE1,T.REFENENCE2,T.REFENENCE3,T.REFENENCE4,T.CREATE_ORG_ID,T.EXEC_ORG_ID,T.UGRT_GRD,T.SALES_MAN,T.ENABLE_FLAG,T.PLAN_STAT,T.LOAD_STAT,T.UNLOAD_STAT,T.BTCH_NUM,T.ALLOW_SPLIT_FLAG,T.COD_FLAG,T.GEN_ORDER_TYP
,T.TOT_QNTY
,T.TRANS_UOM
,T.TOT_GROSS_W
,T.TOT_VOL,T.BIZ_TYP
,T.TOT_NET_W,T.TOT_WORTH,T.TOT_QNTY_EACH,T.SKU_NAME,T.ROUTE_ID,T.PRINT_FLAG,T.SHOW_SEQ,T.SLF_DELIVER_FLAG,T.SLF_PICKUP_FLAG,T.NOTES,T.SUPLR_ID,T.SUPLR_NAME,t6.VEHICLE_TYPE as VEHICLE_TYP_NAME,T.PLATE_NO,T.DRIVER,T.MOBILE,T.ABNOMAL_STAT
,t.addtime
,t.load_print_count --打印次数
,T.ADDWHO,T.EDITTIME,T.EDITWHO
,T.ODR_NO||T.CUSTOMER_NAME||T.CUSTOM_ODR_NO AS FULL_INDEX
,t.CREATE_ORG_ID_NAME,t.EXEC_ORG_ID_NAME,t.STATUS_NAME
,to_char(t.unload_time,'yyyy/mm/dd hh24:mi') as max_unload_time   --yuanlei 2012-2-17
,to_char(t.unload_time,'yyyy/mm/dd hh24:mi') as unload_time
,t.unload_delay_reason
,t.pod_delay_reason
,to_char(t.pod_time,'yyyy/mm/dd hh24:mi') as pod_time
,T.trans_srvc_id_name,T.RECE_STAT,T.RECE_STAT_NAME
,CUSTOMER_NAME AS BILL_TO_NAME
,t.losdam_flag
,0 as TOT_LD_QNTY,0 as TOT_UNLD_QNTY,0 as load_qnty_each,0 as unload_qnty_eac
,pod_delays.name_c as POD_DELAY_REASON_NAME
,unload_delays.name_c as UNLOAD_DELAY_REASON_NAME
,biz.name_c as BIZ_TYP_NAME
,t.load_area_id2,t.load_area_name2,t.unload_area_id2,t.unload_area_name2
,t2.NAME_C AS ODR_TYP_NAME,t.upload_flag
,t3.gen_method --客户月结帐号
,t3.parent_customer_id,t4.short_name parent_customer_name --隶属客户
,T.BILL_PRICE,T.INIT_FLAG
FROM BMS_ORDER_HEADER T
,bas_codes pod_delays --回单延迟
,bas_codes unload_delays --到货延迟
,bas_codes biz
,bas_codes t2
,bas_customer t3
,bas_customer t4
,bas_codes t5
,BAS_VEHICLE_TYPE t6
where t.pod_delay_reason = pod_delays.id(+) and pod_delays.prop_code(+) = 'POD_DELAY_REASON'
and t.unload_delay_reason = unload_delays.id(+) and unload_delays.prop_code(+) = 'UNLOAD_DELAY_REASON'
and t.biz_typ = biz.id(+)
and t.customer_id = t3.id(+)
and t3.parent_customer_id = t4.id(+)
and t.ODR_TYP = t2.id(+) and t2.prop_code(+) = 'TRS_TYP'
and t.AUDIT_STAT = t5.code(+) and t5.prop_code(+)='AUDIT_STAT'
and t.VEHICLE_TYP = t6.id(+) and t6.enable_flag(+)='Y';


create or replace procedure SP_IMPORT_ORDER(
IN_TMPID varchar2,
IN_USERID varchar2,
IN_LANGUAGE VARCHAR2,
OUTPUT_RESULLT in out varchar2)
IS

t_cur_column varchar2(100);
t_cur_value varchar2(500);
t_verify_type number(4);
t_table_name varchar2(100);
t_verify_code varchar2(100);

v_sql varchar2(2048);
temp_sql varchar2(1024);
temp_result varchar2(500);
tmp_verify_code varchar2(100);

r_REFENENCE1 varchar2(100);
r_CUSTOM_ODR_NO varchar2(100);
r_CUSTOMER_NAME varchar2(100);
r_BIZ_TYP varchar2(100);
r_ODR_TYP varchar2(100);
r_ODR_TIME varchar2(100);
r_VEHICLE_TYP varchar2(100);
r_TEMPERATURE1 varchar2(100);
r_TEMPERATURE2 varchar2(100);
r_PRE_LOAD_TIME varchar2(100);
r_PRE_UNLOAD_TIME varchar2(100);
r_SLF_PICKUP_FLAG varchar2(10);
r_SLF_DELIVER_FLAG varchar2(10);
r_NOTES varchar2(500);
r_LOAD_CODE varchar2(100);
r_LOAD_AREA_NAME varchar2(100);
r_LOAD_AREA_NAME2 varchar2(100);
r_LOAD_AREA_NAME3 varchar2(100);
r_LOAD_ADDRESS varchar2(300);
r_LOAD_CONTACT varchar2(100);
r_LOAD_TEL varchar2(100);
r_UNLOAD_CODE varchar2(100);
r_UNLOAD_AREA_NAME varchar2(100);
r_UNLOAD_AREA_NAME2 varchar2(100);
r_UNLOAD_AREA_NAME3 varchar2(100);
r_UNLOAD_ADDRESS varchar2(300);
r_UNLOAD_CONTACT varchar2(100);
r_UNLOAD_TEL varchar2(100);
r_SKU varchar2(100);
r_SKU_NAME varchar2(200);
r_UOM varchar2(100);
r_QNTY varchar2(100);
r_TEMPERATURE varchar2(100);
r_G_WGT varchar2(100);
r_VOL varchar2(100);
r_LINE number(4);
r_USERID varchar2(30);
r_BTCH_NUM VARCHAR2(50);

type   cur_t   is   ref   cursor;
      C_ACT   cur_t;

TYPE array_var IS VARRAY(37) OF nvarchar2(10000);

tmp_key array_var;
tmp_value array_var;

tmp_table varchar2(255);
tmp_column varchar2(255);
tmp_param_type NUMBER;
tmp_replace_field varchar2(100);
tmp_default_value varchar2(255);

exec_sql varchar2(4096);
exec_value varchar2(4096);

r_count number(4);
pos number(4);

load_code varchar2(100);
unload_code varchar2(100);

op_customer_id varchar2(32);
op_customer_name VARCHAR2(50);
op_load_area_name varchar2(50);
op_load_area_name2 varchar2(50);
op_load_area_name3 varchar2(50);
op_unload_area_name varchar2(50);
op_unload_area_name2 varchar2(50);
op_unload_area_name3 varchar2(50);
op_load_address varchar2(300);
op_load_contact varchar2(40);
op_load_tel varchar2(80);
op_unload_address varchar2(300);
op_unload_contact varchar2(40);
op_unload_tel varchar2(80);
op_load_area_id varchar2(32);
op_load_area_id2 varchar2(32);
op_load_area_id3 varchar2(32);
op_load_name varchar2(100);
op_unload_area_id varchar2(32);
op_unload_area_id2 varchar2(32);
op_unload_area_id3 varchar2(32);
op_unload_name varchar2(100);
op_exec_org_id varchar2(32);
op_id varchar2(32);
op_odr_no varchar2(100);
op_odr_row number(4);
op_skuname varchar2(100);
op_packid varchar2(32);

odr_lst LST := LST();

tmp_unload_address varchar2(300);
tmp_unload_contact varchar2(40);
tmp_unload_tel varchar2(40);
tmp_load_address  varchar2(300);
tmp_load_contact varchar2(40);
tmp_load_tel varchar2(80);


CURSOR verify is
      select verify_type,table_name,verify_code from t_input_excel_verify where type_name = 'TMP_ORDER_IMPORT' and COLUMN_NAME = t_cur_column;
CURSOR head is
      select column_name,default_value,param_type,replace_field,table_name from t_input_excel_default where table_name = 'TRANS_ORDER_HEADER' order by exec_seq asc;
CURSOR item is
      select column_name,default_value,param_type,replace_field,table_name from t_input_excel_default where table_name = 'TRANS_ORDER_ITEM' order by exec_seq asc;
--IN_TMPID varchar2(32);
BEGIN
  --IN_TMPID := '1489116521217';
  OUTPUT_RESULLT := '';
  --return;
  tmp_key := array_var('REFENENCE1','CUSTOM_ODR_NO','CUSTOMER_NAME','BIZ_TYP','ODR_TYP','ODR_TIME','VEHICLE_TYP','TEMPERATURE1','TEMPERATURE2','PRE_LOAD_TIME'
  ,'PRE_UNLOAD_TIME','SLF_PICKUP_FLAG','SLF_DELIVER_FLAG','NOTES','LOAD_CODE','LOAD_AREA_NAME','LOAD_AREA_NAME2','LOAD_AREA_NAME3','LOAD_ADDRESS','LOAD_CONTACT'
  ,'LOAD_TEL','UNLOAD_CODE','UNLOAD_AREA_NAME','UNLOAD_AREA_NAME2','UNLOAD_AREA_NAME3','UNLOAD_ADDRESS','UNLOAD_CONTACT','UNLOAD_TEL','SKU','SKU_NAME','UOM'
  ,'QNTY','TEMPERATURE','G_WGT','VOL','BTCH_NUM','USER_ID');
  v_sql := '';
  r_LINE := 1;
  pos := 1;
  for i in 1..tmp_key.count loop
      t_cur_column := tmp_key(i);
      v_sql := v_sql|| t_cur_column || ',';
  end loop;
  v_sql := 'SELECT ' || v_sql || 'LINENO FROM TMP_ORDER_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' ORDER BY LINENO ASC';
  --校验字段
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_PRE_LOAD_TIME
       ,r_PRE_UNLOAD_TIME,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG,r_NOTES,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;

      tmp_value := array_var(r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_PRE_LOAD_TIME
       ,r_PRE_UNLOAD_TIME,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG,r_NOTES,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID);

      if r_CUSTOMER_NAME is null then  --当客户单号为空时，可认为与上一行信息相同，直接跳到明细列进行校验。
          pos := 15;
      end if;

      for i in pos..tmp_key.count loop
           t_cur_column := tmp_key(i);
           t_cur_value := tmp_value(i);
           for m in verify loop
               t_verify_type := m.verify_type;
               t_table_name := m.table_name;
               t_verify_code := m.verify_code;

               if t_verify_type = 0 then  --非空检验
                   if t_cur_value is null or length(t_cur_value) < 1 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不能为空<br />';
                   end if;
               elsif t_verify_type = 1 and length(t_cur_value) > 0 then --唯一性校验
                   temp_sql := 'select count(1) from ' || t_table_name || ' where ' || t_cur_column || '=''' || t_cur_value || '''';
                   execute immediate temp_sql into r_count;
                   if r_count > 0 then
                        OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不是唯一<br />';
                   else
                      temp_sql := 'select count(1) from TMP_ORDER_IMPORT where TMPID = ''' || IN_TMPID || ''' AND USER_ID = '''
                        || IN_USERID || ''' and ' || t_cur_column || '= ''' || t_cur_value || '''';
                      execute immediate temp_sql into r_count;
                      if r_count > 1 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行['|| t_cur_column ||']列不是唯一<br />';
                      end if;
                   end if;
               elsif t_verify_type = 2 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行['|| t_cur_column ||']列值为[' || t_cur_value || '],数值不合法<br />';
                   else
                      if t_cur_column = 'SKU' then  --如果是只填写SKU，则将SKU对应的名称写到临时表，避免校验SKU_NAME时因为无值不通过
                          select sku_cname into op_skuname from BAS_SKU where sku = t_cur_value;
                          update TMP_ORDER_IMPORT set SKU_NAME = op_skuname
                            WHERE TMPID = IN_TMPID AND USER_ID = IN_USERID AND LINENO = r_LINE;
                          tmp_value(30) := op_skuname;
                      end if;
                   end if;
               elsif t_verify_type = 3 then   --二选一非空校验
                   if t_cur_value is null then
                       select verify_code into tmp_verify_code from t_input_excel_verify where column_name = t_verify_code and type_name = 'TMP_ORDER_IMPORT';
                       temp_sql := 'select ' || t_verify_code || ' from TMP_ORDER_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' and LINENO = ''' || r_LINE || '''';
                       execute immediate temp_sql into temp_result;
                       temp_sql := REPLACE(tmp_verify_code, '?', temp_result);
                       execute immediate temp_sql into r_count;
                       if r_count = 0 then
                           OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不合法<br />';
                       end if;
                   end if;
               end if;
           end loop;
      end loop;
      <<next_loop>>
          t_cur_column := '';
  end loop;
  close C_ACT;

  if length(OUTPUT_RESULLT) > 0 then
    OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
    rollback;
    -- 失败删除中间表
    delete from TMP_ORDER_IMPORT where TMPID = IN_TMPID;
    commit;
    return;
  end if;
  --插入数据
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_PRE_LOAD_TIME
       ,r_PRE_UNLOAD_TIME,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG,r_NOTES,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;

      tmp_value := array_var(r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_PRE_LOAD_TIME
       ,r_PRE_UNLOAD_TIME,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG,r_NOTES,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID);

      exec_sql := '';
      exec_value := '';
      if r_CUSTOMER_NAME is not null then
          for h in head loop
              tmp_table := h.table_name;
              tmp_column := h.column_name;
              tmp_param_type := h.param_type;
              tmp_replace_field := h.replace_field;
              tmp_default_value := h.default_value;
              exec_sql := exec_sql || ',' || h.column_name;
              if h.param_type = '0' then  --定制
                  exec_value := exec_value || ',' || h.default_value;
              elsif h.param_type = '1' then  --从执行结果取值
                  temp_sql := h.default_value;
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
                  for i in 1..tmp_key.count loop
                      t_cur_column := tmp_key(i);
                      if t_cur_column = h.Replace_Field then
                          t_cur_value := tmp_value(i);
                          exit;
                      end if;
                  end loop;
                  if t_cur_value is not null then
                      temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                      execute immediate temp_sql into temp_result;
                      exec_value := exec_value || ',''' || temp_result || '''';
                  else
                      exec_value := exec_value || ',NULL';
                  end if;
                  if  h.column_name = 'CUSTOMER_ID' then
                      op_customer_id := temp_result;
                  elsif h.column_name = 'CUSTOMER_NAME' then
                      op_customer_name := temp_result;
                  end if;
              elsif h.param_type = '3' then  --直接从临时表上取值
                  for i in 1..tmp_key.count loop
                      t_cur_column := tmp_key(i);
                      if t_cur_column = h.column_name then
                          t_cur_value := tmp_value(i);
                          exit;
                      end if;
                  end loop;
                  if t_cur_value is null and h.column_name = 'CUSTOMER_NAME' then
                     goto next_loop;
                  end if;
                  if t_cur_value is null then
                      exec_value := exec_value || ',NULL';
                  else
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
             end if;
          end loop;
      end if;
      <<next_loop>>
          OUTPUT_RESULLT := '';
      if length(exec_value) > 1 then
          --获取托运单号
          SP_GET_IDSEQ('ORDER',op_odr_no);

          exec_sql := 'INSERT INTO TRANS_ORDER_HEADER(ODR_NO,ADDWHO,ADDTIME' || exec_sql || ') VALUES(''' || op_odr_no || ''',''' || IN_USERID || ''',sysdate' || exec_value || ')';
          execute immediate exec_sql;

          odr_lst.extend();
          odr_lst(odr_lst.count) := op_odr_no;
      end if;
      exec_sql := '';
      exec_value := '';
      op_packid := '2EEB495B515049B3A9CD76572DDDC999';
      for h in item loop
          tmp_table := h.table_name;
          tmp_column := h.column_name;
          tmp_param_type := h.param_type;
          tmp_replace_field := h.replace_field;
          tmp_default_value := h.default_value;
          if h.param_type = '0' then  --定制
              exec_sql := exec_sql || ',' || h.column_name;
              exec_value := exec_value || ',' || h.default_value;
          elsif h.param_type = '1' then  --从执行结果取值
              exec_sql := exec_sql || ',' || h.column_name;
              temp_sql := h.default_value;
              execute immediate temp_sql into temp_result;
              exec_value := exec_value || ',''' || temp_result || '''';
          elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
              exec_sql := exec_sql || ',' || h.column_name;
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.replace_field then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is not null then
                  temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              else
                  exec_value := exec_value || ',NULL';
              end if;
          elsif h.param_type = '3' then  --直接从临时表上取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.column_name then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if h.column_name = 'LOAD_CODE' then
                  load_code := t_cur_value;
                  if load_code is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_CODE' then
                  unload_code := t_cur_value;
                  if unload_code is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME' then
                  op_unload_area_name := t_cur_value;
                  if op_unload_area_name is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME2' then
                  op_unload_area_name2 := t_cur_value;
                  if op_unload_area_name2 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value  || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME3' then
                  op_unload_area_name3 := t_cur_value;
                  if op_unload_area_name3 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_ADDRESS' then
                  op_unload_address := t_cur_value;
                  if op_unload_address is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_CONTACT' then
                  op_unload_contact := t_cur_value;
                  if op_unload_contact is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_TEL' then
                  op_unload_tel := t_cur_value;
                  if op_unload_tel is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME' then
                  op_load_area_name := t_cur_value;
                  if op_load_area_name is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME2' then
                  op_load_area_name2 := t_cur_value;
                  if op_load_area_name2 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME3' then
                  op_load_area_name3 := t_cur_value;
                  if op_load_area_name3 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_ADDRESS' then
                  op_load_address := t_cur_value;
                  if op_load_address is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_CONTACT' then
                  op_load_contact := t_cur_value;
                  if op_load_contact is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_TEL' then
                  op_load_tel := t_cur_value;
                  if op_load_tel is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'SKU' then
                  if t_cur_value is not null then
                      select id into op_packid from bas_sku where sku = t_cur_value;
                  end if;
                  exec_sql := exec_sql || ',' || h.column_name;
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              else
                  exec_sql := exec_sql || ',' || h.column_name;
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              end if;
         elsif h.param_type = '4' then --发货地址特殊处理
              if load_code is null then  --先自动录入地址信息再处理
                  select F_GET_DEFAULT_ORGBYUSER(IN_USERID, 0) into op_exec_org_id from dual;
                  select sys_guid() into op_id from dual;
                  select area_code into op_load_area_id from bas_area where area_cname=op_load_area_name and area_level=3 and rownum=1;
                  select area_code into op_load_area_id2 from bas_area where area_cname=op_load_area_name2 and area_level=4 and rownum=1;
                  load_code := substr(F_ZJM(op_load_address),0,32);
                  op_load_name := substr(op_load_address,0,100);
                  if op_load_area_name3 is not null then
                      select area_code into op_load_area_id3 from bas_area where area_cname=op_load_area_name3 and area_level=5 and rownum=1;
                  end if;

                  select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = load_code;
                  if r_count > 0 then
                      select id,addr_name,area_id,area_id2,area_id3 into op_id,op_load_name,op_load_area_id,op_load_area_id2,op_load_area_id3
                        from bas_address where customer_id = op_customer_id and addr_code = load_code;
                  else
                      --插入地址
                      insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
                        cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
                      values(op_id,load_code,op_customer_id,op_customer_name,'Y','N','N',op_load_area_id,op_load_area_name,op_load_name,op_load_address,
                        op_load_contact,op_load_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,IN_USERID,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3);
                  end if;
                  --exec_value := exec_value || ',''' || op_id || '''';

                  exec_sql := exec_sql || ',LOAD_ID,LOAD_CODE,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_ID2,LOAD_AREA_ID3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || load_code || ''',''' || op_load_name || ''',''' || op_load_area_id || ''','''
                     || op_load_area_id2 || ''',''' || op_load_area_id3 || '''';
              else
                  select count(1) into r_count from bas_address where addr_code = load_code;
                  if r_count > 1 then
                      select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = load_code;
                      if r_count > 1 or r_count = 0 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行收货方代码['|| unload_code ||']重复<br />';
                          for h in 1..odr_lst.count loop
                              op_odr_no := odr_lst(h);
                              delete from trans_order_header where odr_no = op_odr_no;
                              delete from trans_order_item where odr_no = op_odr_no;
                          end loop;
                          commit;
                          return;
                      else
                          select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address
                          into op_id,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,tmp_load_contact,tmp_load_tel,tmp_load_address
                          from bas_address where customer_id = op_customer_id and addr_code = load_code; 
                      end if;
                  else
                      select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address
                      into op_id,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,tmp_load_contact,tmp_load_tel,tmp_load_address
                      from bas_address where addr_code = load_code;
                  end if;

                  exec_sql := exec_sql || ',LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || op_load_name || ''',''' || op_load_area_id || ''',''' || op_load_area_name ||''','''
                     || op_load_area_id2 || ''',''' || op_load_area_name2 ||''',''' || op_load_area_id3 || ''',''' || op_load_area_name3 || '''';
                  if op_load_address is null then
                      exec_sql := exec_sql || ',LOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_load_address || '''';
                  end if;
                  if op_load_contact is null then
                      exec_sql := exec_sql || ',LOAD_CONTACT';
                      exec_value := exec_value || ',''' || tmp_load_contact || '''';
                  end if;
                  if op_load_tel is null then
                      exec_sql := exec_sql || ',LOAD_TEL';
                      exec_value := exec_value || ',''' || tmp_load_tel || '''';
                  end if;
              end if;
              --exec_value := exec_value || ',' || t_cur_value;
         elsif h.param_type = '5' then --收货地址特殊处理
              if unload_code is null then  --先自动录入地址信息再处理
                  select F_GET_DEFAULT_ORGBYUSER(IN_USERID, 0) into op_exec_org_id from dual;
                  select sys_guid() into op_id from dual;
                  select area_code into op_unload_area_id from bas_area where area_cname=op_unload_area_name and area_level=3 and rownum=1;
                  select area_code into op_unload_area_id2 from bas_area where area_cname=op_unload_area_name2 and area_level=4 and rownum=1;
                  unload_code := substr(F_ZJM(op_unload_address),0,32);
                  op_unload_name := substr(op_unload_address,0,100);
                  if op_unload_area_name3 is not null then
                      select area_code into op_unload_area_id3 from bas_area where area_cname=op_unload_area_name3 and area_level=5 and rownum=1;
                  end if;

                  select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                  if r_count > 0 then
                      select id,addr_name,area_id,area_id2,area_id3 into op_id,op_unload_name,op_unload_area_id,op_unload_area_id2,op_unload_area_id3
                      from bas_address where addr_code = unload_code and customer_id = op_customer_id;
                  else
                      --插入地址
                      insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
                        cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
                      values(op_id,unload_code,op_customer_id,op_customer_name,'N','N','Y',op_unload_area_id,op_unload_area_name,op_unload_name,op_unload_address,
                        op_unload_contact,op_unload_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,IN_USERID,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3);
                  end if;
                  --exec_value := exec_value || ',''' || op_id || '''';

                  exec_sql := exec_sql || ',UNLOAD_ID,UNLOAD_CODE,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_ID2,UNLOAD_AREA_ID3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || unload_code || ''',''' || op_unload_name || ''',''' || op_unload_area_id || ''','''
                     || op_unload_area_id2 || ''',''' || op_unload_area_id3 || '''';
              else
                  select count(1) into r_count from bas_address where addr_code = unload_code;
                  if r_count > 1 then
                      select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                      if r_count > 1 or r_count = 0 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行收货方代码['|| unload_code ||']重复<br />';
                          for h in 1..odr_lst.count loop
                              op_odr_no := odr_lst(h);
                              delete from trans_order_header where odr_no = op_odr_no;
                              delete from trans_order_item where odr_no = op_odr_no;
                          end loop;
                          commit;
                          return;
                      else
                          select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,address,cont_name,cont_tel
                          into op_id,op_unload_name,op_unload_area_id,op_unload_area_name,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3,tmp_unload_address,tmp_unload_contact,tmp_unload_tel
                          from bas_address where customer_id = op_customer_id and addr_code = unload_code; 
                      end if;
                  else
                      select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,address,cont_name,cont_tel
                      into op_id,op_unload_name,op_unload_area_id,op_unload_area_name,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3,tmp_unload_address,tmp_unload_contact,tmp_unload_tel
                      from bas_address where addr_code = unload_code;
                  end if;
                  exec_sql := exec_sql || ',UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || op_unload_name || ''',''' || op_unload_area_id || ''',''' || op_unload_area_name || ''','''
                     || op_unload_area_id2 || ''',''' || op_unload_area_name2 || ''',''' || op_unload_area_id3 || ''',''' || op_unload_area_name3 || '''';
                  if op_unload_address is null then
                      exec_sql := exec_sql || ',UNLOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_unload_address || '''';
                  end if;
                  if op_unload_contact is null then
                      exec_sql := exec_sql || ',UNLOAD_CONTACT';
                      exec_value := exec_value || ',''' || tmp_unload_contact || '''';
                  end if;
                  if op_unload_tel is null then
                      exec_sql := exec_sql || ',UNLOAD_TEL';
                      exec_value := exec_value || ',''' || tmp_unload_tel || '''';
                  end if;
              end if;
              --exec_value := exec_value || ',' || t_cur_value;
         end if;
         <<next_loop>>
             OUTPUT_RESULLT := '';
      end loop;
      select nvl(max(odr_row),0) + 1 into op_odr_row from trans_order_item where odr_no = op_odr_no;
      exec_sql := 'INSERT INTO TRANS_ORDER_ITEM(ODR_NO,ODR_ROW,ADDWHO,PACK_ID,ADDTIME' || exec_sql || ') VALUES(''' || op_odr_no || ''',' || op_odr_row || ',''' || IN_USERID || ''',''' || op_packid ||''',sysdate' || exec_value || ')';
      execute immediate exec_sql;
      
      ORDER_QTY_COUNT(op_odr_no, OUTPUT_RESULLT); --汇总订单信息
      --更新订单发货点和收货点信息
      UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select min(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select max(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      
      OUTPUT_RESULLT := '00';
  end loop;
  close C_ACT;
  commit; 

  delete from TMP_ORDER_IMPORT WHERE TMPID = IN_TMPID;
  /*for h in 1..odr_lst.count loop
      op_odr_no := odr_lst(h);
      ORDER_QTY_COUNT(op_odr_no, OUTPUT_RESULLT); --汇总订单信息
      --更新订单发货点和收货点信息
      UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select min(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select max(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
  end loop;*/
  OUTPUT_RESULLT := '00';
  commit;
  EXCEPTION
    WHEN OTHERS THEN
        OUTPUT_RESULLT :='01'||sqlerrm || ']';
        rollback;
        
        insert into t_input_excel_temp(type_name,VALUE,line,column_name,column_cname,id,custom_odr_no) 
        values(tmp_param_type,tmp_default_value,r_LINE,tmp_column,substr(OUTPUT_RESULLT,0,199),to_char(sysdate,'YYYY-MM-DD HH24:MI'),tmp_table);
        
        delete from TMP_ORDER_IMPORT WHERE TMPID = IN_TMPID;
        for h in 1..odr_lst.count loop
            op_odr_no := odr_lst(h);
            delete from trans_order_header where odr_no = op_odr_no;
            delete from trans_order_item where odr_no = op_odr_no;
        end loop;
        commit;
        --RAISE_APPLICATION_ERROR(-20101, '执行全局校验时抛出异常: '||sqlerrm, TRUE);
END;
/


--t_input_excel_verify
25	BTCH_NUM	1	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT		AAAXMoAAGAAACLcABa
--t_Input_Excel_Default
29	TRANS_ORDER_HEADER	BTCH_NUM		3		1	AAAXMjAAGAAACLOAAA


create or replace view v_shipment_item as
select  trans."ID",trans."SHPM_NO",trans."SHPM_ROW",trans."ODR_NO",trans."PARN_SHPM_NO",
        trans."SKU_ID",trans."SKU_NAME",trans."SKU_ENAME",trans."PACK_ID",
        trans."UOM",trans."QNTY",trans."VOL",trans."VOL_UNIT",trans."G_WGT",trans."N_WGT",
        trans."WGT_UNIT",trans."WORTH",
        trans."UNLD_QNTY",trans.UNLD_VOL,
       trans."UNLD_GWGT",trans."LD_NWGT",trans."LD_WORTH",
        trans."LD_QNTY",trans."LD_VOL",trans."LD_GWGT",trans."UNLD_NWGT",
        trans."UNLD_WORTH",trans."LOTATT01" as LOTATT01,trans."LOTATT02",
        trans."LOTATT03",trans."LOTATT04",
        trans."REFENENCE1",trans."REFENENCE2",trans."REFENENCE3",trans."REFENENCE4",trans."NOTES",
        trans."ADDTIME",trans."ADDWHO",trans."EDITTIME",trans."EDIWHO",trans."ODR_QNTY",
        trans.sku,trans.qnty_each,trans.temperature1,t1.name_c as temperature1_NAME,head.unload_area_id2,
        head.exec_org_id_name,head.plate_no,head.mobile,head.driver,head.unload_tel,head.unload_contact,
        head.unload_address,head.unload_area_name2,head.unload_name,head.load_name,head.UNLOAD_AREA_ID
 from TRANS_SHIPMENT_ITEM trans
 ,TRANS_SHIPMENT_HEADER head
 ,bas_codes t1
 where trans.shpm_no = head.shpm_no
 and  trans.temperature1 = t1.id(+) and t1.prop_code(+) = 'TRANS_COND';

 create or replace view lc_pay_suplr_header as
select to_char(rownum-1) as col1,'C004' as col2,'' as col3,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col4,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col5
,'供应商' as col6,t1.suplr_cname as col7,'业务管理' as col8,'余磊' as col9,'业务管理' as col10,'人民币' as col11,t1.udf1 as col12
 from bill_pay_initial t
 ,bas_supplier t1
 where t.suplr_id  = t1.id(+)
 and t.belong_month = to_char(last_day(add_months(sysdate,-1)),'YYYYMM');
 
 
 create or replace view lc_pay_suplr_detail as
select rownum-1 as col1,'' as col2,'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '01' AS col3,
 'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '-' || t1.short_name as col4,
 'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '-' || t1.short_name as col5,t1.udf2 || '天' as col6,'供应商' as col7,t1.suplr_cname as col8,
 '业务管理' as col9,'余磊' as col10,'长途运输成本Long-distanceTruckingCost' as col11,t3.customer_cname as col12,'人民币' as col13,'1' as col14,t.TOT_AMOUNT as col15,
 t.TOT_AMOUNT as col16,'CN03' as col17,t1.udf2 as col18,'' as col19,'' as col20,'' as col21,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col22,
 '' as col23,t.belong_month as col24,t2.short_name as col25,'' as col26,'国内采购' as col27
 from bill_pay_initdetails t
 ,bas_supplier t1
 ,BAS_CUSTOMER t2
 ,bas_customer t3
 where t.suplr_id = t1.id(+)
 and t.customer_id = t2.id(+)
 and t2.parent_customer_id = t3.id(+);


create or replace view lc_pay_customer_header as
select to_char(rownum-1) as col1,'C004' as col2,'' as col3,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col4,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col5
,'供应商' as col6,t1.suplr_cname as col7,'业务管理' as col8,'余磊' as col9,'业务管理' as col10,'人民币' as col11,t1.udf1 as col12
 from bill_pay_initial t
 ,bas_supplier t1
 where t.suplr_id  = t1.id(+)
 and t.belong_month = to_char(last_day(add_months(sysdate,-1)),'YYYYMM');


create or replace view lc_pay_customer_detail as
select rownum-1 as col1,'' as col2,'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '01' AS col3,
 'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '-' || t1.short_name as col4,
 'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '-' || t1.short_name as col5,t1.udf2 || '天' as col6,'供应商' as col7,t1.suplr_cname as col8,
 '业务管理' as col9,'余磊' as col10,'长途运输成本Long-distanceTruckingCost' as col11,t3.customer_cname as col12,'人民币' as col13,'1' as col14,t.TOT_AMOUNT as col15,
 t.TOT_AMOUNT as col16,'CN03' as col17,t1.udf2 as col18,'' as col19,'' as col20,'' as col21,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col22,
 '' as col23,t.belong_month as col24,t2.short_name as col25,'' as col26,'国内采购' as col27
 from bill_pay_initdetails t
 ,bas_supplier t1
 ,BAS_CUSTOMER t2
 ,bas_customer t3
 where t.suplr_id = t1.id(+)
 and t.customer_id = t2.id(+)
 and t2.parent_customer_id = t3.id(+);


create or replace view lc_rec_detail as
select rownum-1 as col1,'' as col2,'R' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '01' AS col3,
 'R' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '-' || t1.short_name as col4,
 t1.short_name || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') as col5,t1.udf2 || '天' as col6,'客户' as col7,t2.customer_cname as col8,
 '' as col9,t1.udf4 as col10,'夏乙禾' as col11,'人民币' as col12,t.TOT_AMOUNT as col13,'1' as col14,t.TOT_AMOUNT as col15,
 'CN06' as col16,t1.udf2 as col17,'' as col18,'' as col19,'' as col20,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col21,
 t.buss_name as col22,t.belong_month as col23,'' as col24,'国内销售' as col25
 from bill_rec_initdetails t
 ,bas_customer t1
 ,bas_customer t2
 where t.buss_id = t1.id
 and t1.parent_customer_id = t2.id(+);

create or replace view lc_rec_header as
select to_char(rownum-1) as col1,'' as col2,'C004' as col3,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col4,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col5
,'客户' as col6,t2.customer_cname as col7,'客服' as col8,'夏乙禾' as col9,'客服' as col10,'人民币' as col11,t1.udf1 as col12
 from bill_rec_initial t
 ,bas_customer t1
 ,Bas_customer t2
 where t.buss_id  = t1.id
 and t1.parent_customer_id = t2.id(+)
 and t.belong_month = to_char(last_day(add_months(sysdate,-1)),'YYYYMM');


-- Create table
create table SYS_WARN_SETTING
(
  ID         VARCHAR2(32) not null,
  WARN_TYPE  VARCHAR2(32),
  WARN_DESCR VARCHAR2(32),
  ADDTIME    DATE not null,
  ADDWHO     VARCHAR2(32) not null,
  EDITTIME   DATE,
  EDITWHO    VARCHAR2(32)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column SYS_WARN_SETTING.WARN_TYPE
  is '预警类型';
comment on column SYS_WARN_SETTING.WARN_DESCR
  is '预警描述';


-- Create table
create table SYS_WARN_MAIL
(
  ID          VARCHAR2(32),
  EMAIL       VARCHAR2(100),
  USERNAME    VARCHAR2(50),
  ACTIVE_FLAG CHAR(1),
  ADDTIME     DATE,
  ADDWHO      VARCHAR2(30),
  EDITTIME    DATE,
  EDITWHO     VARCHAR2(30),
  SETT_ID     VARCHAR2(32),
  MAIL_LEVEL  NUMBER(4)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );


-- Create table
create table SYS_WARN_SMS
(
  ID          VARCHAR2(32),
  MOBILE      VARCHAR2(20),
  USERNAME    VARCHAR2(50),
  ACTIVE_FLAG CHAR(1),
  ADDTIME     DATE not null,
  ADDWHO      VARCHAR2(20) not null,
  EDITTIME    DATE,
  EDITWHO     VARCHAR2(20),
  SETT_ID     VARCHAR2(32),
  SMS_LEVEL   NUMBER(4)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );


insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('3B8A175A969A49B1A81A049DB7311A76', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:54:38', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, 'C333FDD2A38D4444BFBAD0763122FFC3', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('D808B5427F8C4B4B9674BAB03B7F8931', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:50:39', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '5709FCF3E9254FB58815DC0BDFB81394', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('2FE2B437A3104C8283F84F297029A466', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:54:08', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '8FBBD30A583A4A4EAFDABC64D9C6923D', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('09B3DC8370BF43FEA459F87BCD3FD503', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:55:05', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '7020544EEF024CF4867EEB75B91719E3', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('D8F1939E541D46B4B948E3059F65B2E4', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:55:47', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '7020544EEF024CF4867EEB75B91719E3', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('6AC635838F884943B9E3B47008EE2507', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:55:55', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, 'C333FDD2A38D4444BFBAD0763122FFC3', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('6AA1C6901D304A738D673AB589253DF1', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:56:04', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '8FBBD30A583A4A4EAFDABC64D9C6923D', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('8FE230289DAA451EA7CE29FF1FB454D8', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:52:44', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', to_date('05-05-2017 08:56:29', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', '6D48742C67E843B68C6A2A958BF8A660', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('B2FF8F6A2A654E4D9D32BAE2EEFB6C03', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:51:41', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', to_date('05-05-2017 08:52:08', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', '7216FF5365C943BF8B3A52F9D69624D7', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('7E7270023864436B9BFDB4450700A4C1', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:53:42', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, 'CAA7DFDC4DED4872A85435018D201EDD', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('0CE09D8C6886409A912C1A45A30F2EE3', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:56:40', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '6D48742C67E843B68C6A2A958BF8A660', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('007DDBE71F7E4C9E8A41E44DC9326CDB', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:56:47', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '7216FF5365C943BF8B3A52F9D69624D7', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('183CFB382EA041E0A5E61B3BE1378BAE', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:56:57', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '978A950C22CC402DA0BA593ABBE2AD7A', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('2CC0CB18D9BE417FA7C719F2F6BC1E98', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:57:05', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '5709FCF3E9254FB58815DC0BDFB81394', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('FFB2A05B869845C19A02B801D9D5CEB1', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:51:14', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, '978A950C22CC402DA0BA593ABBE2AD7A', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('68EDB0B9C1DA4ADEB02B9BA6939E175E', 'sandy@56infor.cn', '袁磊', 'Y', to_date('05-05-2017 08:53:17', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', to_date('05-05-2017 08:56:24', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', 'C6FA4178F05942678C1E1A66C722B234', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('5FB38631ED93435EAF438A48BCF3CDD4', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:56:10', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, 'CAA7DFDC4DED4872A85435018D201EDD', null);
insert into SYS_WARN_MAIL (ID, EMAIL, USERNAME, ACTIVE_FLAG, ADDTIME, ADDWHO, EDITTIME, EDITWHO, SETT_ID, MAIL_LEVEL)
values ('CFBFEAC368964E76AA8678BFE100D896', 'sandy_23678@163.com', '袁磊', 'Y', to_date('05-05-2017 08:56:21', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null, 'C6FA4178F05942678C1E1A66C722B234', null);
commit;

insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('7020544EEF024CF4867EEB75B91719E3', '911B74863C134D4CB13CF987622D35E0', '应收超期提醒', to_date('05-05-2017 08:54:55', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('978A950C22CC402DA0BA593ABBE2AD7A', '4BB4C975A89340468AE8595F3983D12C', '预约到货提醒', to_date('05-05-2017 08:51:01', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('C333FDD2A38D4444BFBAD0763122FFC3', '876677D41D0148E2AAFE0248684CC787', '承运商付款超期提醒', to_date('05-05-2017 08:54:21', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('C6FA4178F05942678C1E1A66C722B234', 'DB83E760F14044D1866012F6A02B00B1', '车辆保险到期提醒', to_date('05-05-2017 08:52:59', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('8FBBD30A583A4A4EAFDABC64D9C6923D', '5CC2EB782B45497FACF1B270F8A570BF', '未配车确认提醒', to_date('05-05-2017 08:53:54', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('5709FCF3E9254FB58815DC0BDFB81394', 'E24A769BE0BD45969AD1E85DE673D1DE', '客户投诉提醒', to_date('05-05-2017 08:50:24', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('7216FF5365C943BF8B3A52F9D69624D7', '0D266BB70B9849039461DA32DFCDA02E', '客户合同到期提醒', to_date('05-05-2017 08:51:28', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('6D48742C67E843B68C6A2A958BF8A660', '76D837B31294470DABB7FA2AA63F0C37', '承运商合同到期提醒', to_date('05-05-2017 08:52:26', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
insert into SYS_WARN_SETTING (ID, WARN_TYPE, WARN_DESCR, ADDTIME, ADDWHO, EDITTIME, EDITWHO)
values ('CAA7DFDC4DED4872A85435018D201EDD', 'EF768C285FD545229DDA2B022736B782', '车辆年审到期提醒', to_date('05-05-2017 08:53:31', 'dd-mm-yyyy hh24:mi:ss'), 'wpsadmin', null, null);
commit;
create or replace view warn_complaint as
select ODR_NO --托运单号
  ,CUSTOMER_NAME --客户
  ,to_char(LOAD_TIME,'YYYY-MM-DD') as LOAD_TIME  --发货日期
  ,to_char(RECE_TIME,'YYYY-MM-DD') as UNLOAD_TIME --收货日期
  ,LOAD_ADDRESS  --发货地
  ,UNLOAD_ADDRESS --收货地
  ,LOAD_NO --调度单号
  ,PLATE_NO --车牌号
  ,DRIVER --司机
  ,MOBILE --电话
  ,DUTY_TO --责任人
  ,ADDWHO --创建人
  ,to_char(ADDTIME,'YYYY-MM-DD HH24:MI') AS ADDTIME --创建时间
   from trans_complaint where status = '6D926C9A2826437AAEAE0E14119D9DD9';\
   

create or replace view warn_customer_contact as
select TFF_NAME --协议名称
,CONTACT_NO --合同号
,to_char(START_DATE,'YYYY-MM-DD') as START_DATE  --生效日期
,to_char(END_DATE,'YYYY-MM-DD') as END_DATE --失效日期
,OBJECT_NAME  --客户
 from tariff_header where tff_typ = '42666CA2DE904F6687FC172138CF3E51' and end_date - sysdate < 30
 and ENABLE_FLAG = 'Y';


create or replace view warn_loadno_audit as
select t.load_no  --调度单号
,t.suplr_name --承运商
,t.START_AREA_NAME --起点城市
,t.END_AREA_NAME --终点城市
,t.PLATE_NO --车牌号
,t.driver1 --司机
,t.mobile1 --电话
,t.TRAILER_NO --挂车号
,t.addwho  --创建人
,to_char(t.addtime,'YYYY-MM-DD HH24:MI') as ADDTIME --创建时间
 from trans_load_header t
where status = '10' and dispatch_stat = '748EE77B344740D5AAB4BC825DBAF34F';


create or replace view warn_order_audit as
select t.odr_no  --托运单号
,t.custom_odr_no --客户单号
,t.customer_name  --客户
,t2.NAME_C AS ODR_TYP_NAME --订单类型
,t1.NAME_C AS BIZ_TYP_NAME  --运输类型
,to_char(t.addtime,'YYYY-MM-DD HH24:MI') as ADDTIME --录单时间
,to_char(t.pre_load_time,'YYYY-MM-DD HH24:MI') as PRE_LOAD_TIME --要求发货时间
,to_char(t.pre_unload_time,'YYYY-MM-DD HH24:MI') as PRE_UNLOAD_TIME  --要求到货时间
,t3.vehicle_type as VEHICLE_TYP_NAME --车型要求
,t4.email --
 from trans_order_header T
,BAS_CODES t1
,BAS_CODES t2
,BAS_VEHICLE_TYPE t3
,SYS_USER t4
 where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'
  and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'
  and t.vehicle_typ = t3.id(+)
  and t.addwho = t4.user_id
  and t4.email is not null
  and t.status = '10'
  order by t4.email;

create or replace view warn_suplr_contact as
select TFF_NAME --协议名称
,CONTACT_NO --合同号
,to_char(START_DATE,'YYYY-MM-DD') as START_DATE  --生效日期
,to_char(END_DATE,'YYYY-MM-DD') as END_DATE --失效日期
,OBJECT_NAME  --承运商
from tariff_header where tff_typ = '55CDDBECF2BF49D1A451F09A263F3F47' and end_date - sysdate < 30;

create or replace view warn_unload_appoint as
select t.load_no  --调度单号
  ,t.customer_name --客户
  ,t.shpm_no --作业单号
  ,t.load_name --发货地
  ,t.unload_name --收货地
  ,t.tot_qnty  --数量
  ,t.tot_gross_w --毛重
  ,t.tot_vol  --体积
  ,to_char(t.pre_unload_time,'YYYY-MM-DD HH24:MI') as pre_unload_time --预计到货时间
  ,floor((t.pre_unload_time - sysdate)*24) as REMAIN_HOURS --剩余时长

   from trans_shipment_header t
  ,bas_address t1
  where t.unload_id = t1.id and t.pre_unload_time is not null
  and t1.APPOINT_FLAG = 'Y' and floor(t.pre_unload_time - sysdate) <= t1.APPOINT_HOURS;

create or replace view warn_vhe_insur as
select t1.plate_no  --车牌号
,t2.VEHICLE_TYPE as VEHICLE_TYP_ID_NAME  --车型
,t3.SUPLR_CNAME AS SUPLR_ID_NAME --承运商
,t4.name_c as VEHICLE_STAT_NAME  --车辆状态
,t5.name_c as VEHICLE_ATTR_NAME  --车辆属性
,to_char(t.ins_to,'YYYY-MM-DD') as INS_TO --保险到期日
,floor(ins_to - sysdate) as REMAIN_DAYS --剩余天数
from ins_purchase_record t
,bas_vehicle t1
,bas_vehicle_type t2
,BAS_SUPPLIER t3
,bas_codes t4
,bas_codes t5
where t.plate_no = t1.plate_no
and t1.vehicle_typ_id = t2.id
and t1.SUPLR_ID = t3.ID(+)
and t1.vehicle_stat = t4.id(+)
AND t1.VEHICLE_ATTR = t5.ID(+)
and t.ins_to is not null and t.ins_to - sysdate < 30
and t1.vehicle_attr in ('C3CD595673ED4FD5BB80C1D6BCA17C54','527B04599982476FBFFC79D2E64614A4');

--caijiante  2017-05-09
--sys_func_page
961?	22962	P06_T043_P0_06	P06_T043_P0	导入	150	Y	2015/5/27	wpsadmin	导入	T	

--yuanlei 2017-05-09
create table TMP_INSUR_IMPORT
(
  INS_NO      VARCHAR2(50),
  PLATE_NO    VARCHAR2(50),
  INS_DATE    VARCHAR2(50),
  INS_COMPANY VARCHAR2(100),
  INS_TYPE    VARCHAR2(50),
  INS_AMOUNT  NUMBER(10,2),
  INS_FEE     NUMBER(8,2),
  USER_ID     VARCHAR2(30),
  TMPID       VARCHAR2(50),
  LINENO      NUMBER(4),
  ADDTIME     DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
  
  create table TMP_VEHVERIFY_IMPORT
(
  PLATE_NO       VARCHAR2(50),
  SUPLR_NAME     VARCHAR2(50),
  VERIFY_DATE    VARCHAR2(50),
  NEXT_DATE      VARCHAR2(50),
  VERIFY_AMOUNT  NUMBER(8,2),
  VERIFY_ADDRESS VARCHAR2(200),
  USER_ID        VARCHAR2(30),
  TMPID          VARCHAR2(50),
  LINENO         NUMBER(4),
  ADDTIME        DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );

-- Create table
create table TMP_REPAIR_IMPORT
(
  PLATE_NO       VARCHAR2(50),
  PRE_START_TIME VARCHAR2(50),
  REPAIR_OBJECT  VARCHAR2(32),
  TOTAL_AMOUNT   NUMBER(8,2),
  REPAIR_ADDRESS VARCHAR2(400),
  USER_ID        VARCHAR2(30),
  TMPID          VARCHAR2(50),
  LINENO         NUMBER(4),
  ADDTIME        DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
  
  create or replace procedure SP_IMPORT_INSUR(
IN_TMPID varchar2, 
IN_USERID varchar2,
IN_LANGUAGE VARCHAR2,
OUTPUT_RESULLT in out varchar2)
IS

t_cur_column varchar2(100);
t_cur_value varchar2(500);
t_verify_type number(4);
t_table_name varchar2(100);
t_verify_code varchar2(100);

v_sql varchar2(2048);
temp_sql varchar2(1024);
temp_result varchar2(500);

r_INS_NO varchar2(50);
r_PLATE_NO varchar2(50);
r_INS_COMPANY varchar2(100);
r_INS_TYPE varchar2(50);
r_INS_FEE NUMBER(10,2);
r_INS_AMOUNT NUMBER(10,2);
r_INS_DATE varchar2(50);

r_LINE number(4);
r_USERID varchar2(30);

type   cur_t   is   ref   cursor;
      C_ACT   cur_t;

TYPE array_var IS VARRAY(36) OF nvarchar2(10000); 

tmp_key array_var; 
tmp_value array_var;


exec_sql varchar2(1024);
exec_value varchar2(1024);

r_count number(4);
pos number(4);

CURSOR verify is
      select verify_type,table_name,verify_code from t_input_excel_verify where type_name = 'TMP_INSUR_IMPORT' and COLUMN_NAME = t_cur_column;
CURSOR head is
      select column_name,default_value,param_type,replace_field from t_input_excel_default where table_name = 'INS_PURCHASE_RECORD' order by exec_seq asc;
--IN_TMPID varchar2(32);
BEGIN
  --IN_TMPID := '1489116521217';
  OUTPUT_RESULLT := '';
  tmp_key := array_var('INS_NO','PLATE_NO','INS_DATE','INS_COMPANY','INS_TYPE','INS_AMOUNT','INS_FEE','USER_ID');
  v_sql := '';
  r_LINE := 1;
  pos := 1;
  for i in 1..tmp_key.count loop
      t_cur_column := tmp_key(i);
      v_sql := v_sql|| t_cur_column || ',';
  end loop;
  v_sql := 'SELECT ' || v_sql || 'LINENO FROM TMP_INSUR_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' ORDER BY LINENO ASC';
  --校验字段
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_INS_NO,r_PLATE_NO,r_INS_DATE,r_INS_COMPANY,r_INS_TYPE,r_INS_AMOUNT,r_INS_FEE,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;
      
      tmp_value := array_var(r_INS_NO,r_PLATE_NO,r_INS_DATE,r_INS_COMPANY,r_INS_TYPE
       ,r_INS_AMOUNT,r_INS_FEE,r_USERID);
      
      for i in pos..tmp_key.count loop
           t_cur_column := tmp_key(i);
           t_cur_value := tmp_value(i);
           for m in verify loop
               t_verify_type := m.verify_type;
               t_table_name := m.table_name;
               t_verify_code := m.verify_code;
               
               if t_verify_type = 0 then  --非空检验
                   if t_cur_value is null or length(t_cur_value) < 1 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不能为空<br />';
                   end if;    
               elsif t_verify_type = 1 and length(t_cur_value) > 0 then --唯一性校验
                   temp_sql := 'select count(1) from ' || t_table_name || ' where ' || t_cur_column || '=''' || t_cur_value || '''';
                   execute immediate temp_sql into r_count;
                   if r_count > 0 then
                        OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不是唯一<br />';
                   else
                      temp_sql := 'select count(1) from TMP_ORDER_IMPORT where TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' 
                        || IN_USERID || ''' and ' || t_cur_column || '= ''' || t_cur_value || '''';
                      execute immediate temp_sql into r_count;
                      if r_count > 1 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不是唯一<br />';
                      end if;
                   end if;
               elsif t_verify_type = 2 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不合法<br />';
                   end if;
               end if;
           end loop;
      end loop;
      <<next_loop>>
          t_cur_column := '';
  end loop;
  close C_ACT;

  if length(OUTPUT_RESULLT) > 0 then
    OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
    rollback;
    -- 失败删除中间表
    --delete from TMP_ORDER_IMPORT where TMPID = IN_TMPID and USER_ID = IN_USERID;
    --commit;
    return;
  end if;
  --插入数据
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_INS_NO,r_PLATE_NO,r_INS_DATE,r_INS_COMPANY,r_INS_TYPE,r_INS_AMOUNT,r_INS_FEE,r_USERID,r_LINE;
      exit when C_ACT%notfound;
      
      tmp_value := array_var(r_INS_NO,r_PLATE_NO,r_INS_DATE,r_INS_COMPANY,r_INS_TYPE
       ,r_INS_AMOUNT,r_INS_FEE,r_USERID);
       
      exec_sql := '';
      exec_value := '';
      for h in head loop
          exec_sql := exec_sql || ',' || h.column_name;
          if h.param_type = '0' then  --定制
              exec_value := exec_value || ',' || h.default_value;
          elsif h.param_type = '1' then  --从执行结果取值
              temp_sql := h.default_value;
              execute immediate temp_sql into temp_result;
              exec_value := exec_value || ',''' || temp_result || '''';
          elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.Replace_Field then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is not null then
                  temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              else
                  exec_value := exec_value || ',NULL';
              end if;
          elsif h.param_type = '3' then  --直接从临时表上取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.column_name then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is null then
                  exec_value := exec_value || ',NULL';
              else
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              end if;
         end if;
      end loop; 
      <<next_loop>>
          OUTPUT_RESULLT := '';
      if length(exec_value) > 1 then     
          
          exec_sql := 'INSERT INTO INS_PURCHASE_RECORD(ADDWHO,ADDTIME' || exec_sql || ') VALUES(''' || IN_USERID || ''',sysdate' || exec_value || ')';
          execute immediate exec_sql;
      end if;
      OUTPUT_RESULLT := '00';
  end loop;
  close C_ACT;
  commit;
  OUTPUT_RESULLT := '00';
  delete from TMP_INSUR_IMPORT WHERE TMPID = IN_TMPID AND USER_ID = IN_USERID;
  commit;
  EXCEPTION
    WHEN OTHERS THEN
        OUTPUT_RESULLT :='01'||sqlerrm || '[' || exec_sql || ']';
        rollback;
        --RAISE_APPLICATION_ERROR(-20101, '执行全局校验时抛出异常: '||sqlerrm, TRUE);
END;

/

create or replace procedure SP_IMPORT_VEHVERIFY(
IN_TMPID varchar2, 
IN_USERID varchar2,
IN_LANGUAGE VARCHAR2,
OUTPUT_RESULLT in out varchar2)
IS

t_cur_column varchar2(100);
t_cur_value varchar2(500);
t_verify_type number(4);
t_table_name varchar2(100);
t_verify_code varchar2(100);

v_sql varchar2(2048);
temp_sql varchar2(1024);
temp_result varchar2(500);

r_SUPLR_NAME varchar2(50);
r_PLATE_NO varchar2(50);
r_NEXT_DATE varchar2(50);
r_VERIFY_ADDRESS varchar2(200);
r_VERIFY_AMOUNT NUMBER(10,2);
r_VERIFY_DATE varchar2(50);

r_LINE number(4);
r_USERID varchar2(30);

type   cur_t   is   ref   cursor;
      C_ACT   cur_t;

TYPE array_var IS VARRAY(36) OF nvarchar2(10000); 

tmp_key array_var; 
tmp_value array_var;


exec_sql varchar2(1024);
exec_value varchar2(1024);

r_count number(4);
pos number(4);

CURSOR verify is
      select verify_type,table_name,verify_code from t_input_excel_verify where type_name = 'TMP_VEHVERIFY_IMPORT' and COLUMN_NAME = t_cur_column;
CURSOR head is
      select column_name,default_value,param_type,replace_field from t_input_excel_default where table_name = 'TRANS_VEH_VERIFY' order by exec_seq asc;
--IN_TMPID varchar2(32);
BEGIN
  --IN_TMPID := '1489116521217';
  OUTPUT_RESULLT := '';
  tmp_key := array_var('PLATE_NO','SUPLR_NAME','VERIFY_DATE','NEXT_DATE','VERIFY_AMOUNT','VERIFY_ADDRESS','USER_ID');
  v_sql := '';
  r_LINE := 1;
  pos := 1;
  for i in 1..tmp_key.count loop
      t_cur_column := tmp_key(i);
      v_sql := v_sql|| t_cur_column || ',';
  end loop;
  v_sql := 'SELECT ' || v_sql || 'LINENO FROM TMP_VEHVERIFY_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' ORDER BY LINENO ASC';
  --校验字段
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_PLATE_NO,r_SUPLR_NAME,r_VERIFY_DATE,r_NEXT_DATE,r_VERIFY_AMOUNT,r_VERIFY_ADDRESS,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;
      
      tmp_value := array_var(r_PLATE_NO,r_SUPLR_NAME,r_VERIFY_DATE,r_NEXT_DATE,r_VERIFY_AMOUNT,r_VERIFY_ADDRESS,r_USERID);
      
      for i in pos..tmp_key.count loop
           t_cur_column := tmp_key(i);
           t_cur_value := tmp_value(i);
           for m in verify loop
               t_verify_type := m.verify_type;
               t_table_name := m.table_name;
               t_verify_code := m.verify_code;
               
               if t_verify_type = 0 then  --非空检验
                   if t_cur_value is null or length(t_cur_value) < 1 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不能为空<br />';
                   end if;    
               elsif t_verify_type = 1 and length(t_cur_value) > 0 then --唯一性校验
                   temp_sql := 'select count(1) from ' || t_table_name || ' where ' || t_cur_column || '=''' || t_cur_value || '''';
                   execute immediate temp_sql into r_count;
                   if r_count > 0 then
                        OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不是唯一<br />';
                   else
                      temp_sql := 'select count(1) from TMP_ORDER_IMPORT where TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' 
                        || IN_USERID || ''' and ' || t_cur_column || '= ''' || t_cur_value || '''';
                      execute immediate temp_sql into r_count;
                      if r_count > 1 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不是唯一<br />';
                      end if;
                   end if;
               elsif t_verify_type = 2 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不合法<br />';
                   end if;
               end if;
           end loop;
      end loop;
      <<next_loop>>
          t_cur_column := '';
  end loop;
  close C_ACT;

  if length(OUTPUT_RESULLT) > 0 then
    OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
    rollback;
    -- 失败删除中间表
    --delete from TMP_ORDER_IMPORT where TMPID = IN_TMPID and USER_ID = IN_USERID;
    --commit;
    return;
  end if;
  --插入数据
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_PLATE_NO,r_SUPLR_NAME,r_VERIFY_DATE,r_NEXT_DATE,r_VERIFY_AMOUNT,r_VERIFY_ADDRESS,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;
      
      tmp_value := array_var(r_PLATE_NO,r_SUPLR_NAME,r_VERIFY_DATE,r_NEXT_DATE,r_VERIFY_AMOUNT
       ,r_VERIFY_ADDRESS,r_USERID);
       
      exec_sql := '';
      exec_value := '';
      for h in head loop
          exec_sql := exec_sql || ',' || h.column_name;
          if h.param_type = '0' then  --定制
              exec_value := exec_value || ',' || h.default_value;
          elsif h.param_type = '1' then  --从执行结果取值
              temp_sql := h.default_value;
              execute immediate temp_sql into temp_result;
              exec_value := exec_value || ',''' || temp_result || '''';
          elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.Replace_Field then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is not null then
                  temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              else
                  exec_value := exec_value || ',NULL';
              end if;
          elsif h.param_type = '3' then  --直接从临时表上取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.column_name then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is null then
                  exec_value := exec_value || ',NULL';
              else
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              end if;
         end if;
      end loop; 
      <<next_loop>>
          OUTPUT_RESULLT := '';
      if length(exec_value) > 1 then     
          
          exec_sql := 'INSERT INTO TRANS_VEH_VERIFY(ADDWHO,ADDTIME' || exec_sql || ') VALUES(''' || IN_USERID || ''',sysdate' || exec_value || ')';
          execute immediate exec_sql;
      end if;
      OUTPUT_RESULLT := '00';
  end loop;
  close C_ACT;
  commit;
  OUTPUT_RESULLT := '00';
  delete from TMP_VEHVERIFY_IMPORT WHERE TMPID = IN_TMPID AND USER_ID = IN_USERID;
  commit;
  EXCEPTION
    WHEN OTHERS THEN
        OUTPUT_RESULLT :='01'||sqlerrm || '[' || exec_sql || ']';
        rollback;
        --RAISE_APPLICATION_ERROR(-20101, '执行全局校验时抛出异常: '||sqlerrm, TRUE);
END;

/

create or replace procedure SP_IMPORT_VEHREPAIR(
IN_TMPID varchar2, 
IN_USERID varchar2,
IN_LANGUAGE VARCHAR2,
OUTPUT_RESULLT in out varchar2)
IS

t_cur_column varchar2(100);
t_cur_value varchar2(500);
t_verify_type number(4);
t_table_name varchar2(100);
t_verify_code varchar2(100);

v_sql varchar2(2048);
temp_sql varchar2(1024);
temp_result varchar2(500);

r_PLATE_NO varchar2(50);
r_REPAIR_OBJECT varchar2(32);
r_REPAIR_ADDRESS varchar2(400);
r_TOTAL_AMOUNT NUMBER(10,2);
r_PRE_START_TIME varchar2(50);

r_LINE number(4);
r_USERID varchar2(30);

type   cur_t   is   ref   cursor;
      C_ACT   cur_t;

TYPE array_var IS VARRAY(36) OF nvarchar2(10000); 

tmp_key array_var; 
tmp_value array_var;


exec_sql varchar2(1024);
exec_value varchar2(1024);

r_count number(4);
pos number(4);

CURSOR verify is
      select verify_type,table_name,verify_code from t_input_excel_verify where type_name = 'TMP_REPAIR_IMPORT' and COLUMN_NAME = t_cur_column;
CURSOR head is
      select column_name,default_value,param_type,replace_field from t_input_excel_default where table_name = 'TRANS_VEH_REPAIR' order by exec_seq asc;
--IN_TMPID varchar2(32);
BEGIN
  --IN_TMPID := '1489116521217';
  OUTPUT_RESULLT := '';
  tmp_key := array_var('PLATE_NO','PRE_START_TIME','REPAIR_OBJECT','TOTAL_AMOUNT','REPAIR_ADDRESS','USER_ID');
  v_sql := '';
  r_LINE := 1;
  pos := 1;
  for i in 1..tmp_key.count loop
      t_cur_column := tmp_key(i);
      v_sql := v_sql|| t_cur_column || ',';
  end loop;
  v_sql := 'SELECT ' || v_sql || 'LINENO FROM TMP_REPAIR_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' ORDER BY LINENO ASC';
  --校验字段
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_PLATE_NO,r_PRE_START_TIME,r_REPAIR_OBJECT,r_TOTAL_AMOUNT,r_REPAIR_ADDRESS,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;
      
      tmp_value := array_var(r_PLATE_NO,r_PRE_START_TIME,r_REPAIR_OBJECT,r_TOTAL_AMOUNT,r_REPAIR_ADDRESS,r_USERID);
      
      for i in pos..tmp_key.count loop
           t_cur_column := tmp_key(i);
           t_cur_value := tmp_value(i);
           for m in verify loop
               t_verify_type := m.verify_type;
               t_table_name := m.table_name;
               t_verify_code := m.verify_code;
               
               if t_verify_type = 0 then  --非空检验
                   if t_cur_value is null or length(t_cur_value) < 1 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不能为空<br />';
                   end if;    
               elsif t_verify_type = 1 and length(t_cur_value) > 0 then --唯一性校验
                   temp_sql := 'select count(1) from ' || t_table_name || ' where ' || t_cur_column || '=''' || t_cur_value || '''';
                   execute immediate temp_sql into r_count;
                   if r_count > 0 then
                        OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不是唯一<br />';
                   else
                      temp_sql := 'select count(1) from TMP_ORDER_IMPORT where TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' 
                        || IN_USERID || ''' and ' || t_cur_column || '= ''' || t_cur_value || '''';
                      execute immediate temp_sql into r_count;
                      if r_count > 1 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不是唯一<br />';
                      end if;
                   end if;
               elsif t_verify_type = 2 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不合法<br />';
                   end if;
               end if;
           end loop;
      end loop;
      <<next_loop>>
          t_cur_column := '';
  end loop;
  close C_ACT;

  if length(OUTPUT_RESULLT) > 0 then
    OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
    rollback;
    -- 失败删除中间表
    --delete from TMP_ORDER_IMPORT where TMPID = IN_TMPID and USER_ID = IN_USERID;
    --commit;
    return;
  end if;
  --插入数据
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_PLATE_NO,r_PRE_START_TIME,r_REPAIR_OBJECT,r_TOTAL_AMOUNT,r_REPAIR_ADDRESS,r_USERID,r_LINE;
      exit when C_ACT%notfound;
      
      tmp_value := array_var(r_PLATE_NO,r_PRE_START_TIME,r_REPAIR_OBJECT,r_TOTAL_AMOUNT,r_REPAIR_ADDRESS,r_USERID);
       
      exec_sql := '';
      exec_value := '';
      for h in head loop
          exec_sql := exec_sql || ',' || h.column_name;
          if h.param_type = '0' then  --定制
              exec_value := exec_value || ',' || h.default_value;
          elsif h.param_type = '1' then  --从执行结果取值
              temp_sql := h.default_value;
              execute immediate temp_sql into temp_result;
              exec_value := exec_value || ',''' || temp_result || '''';
          elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.Replace_Field then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is not null then
                  temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              else
                  exec_value := exec_value || ',NULL';
              end if;
          elsif h.param_type = '3' then  --直接从临时表上取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.column_name then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is null then
                  exec_value := exec_value || ',NULL';
              else
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              end if;
         end if;
      end loop; 
      <<next_loop>>
          OUTPUT_RESULLT := '';
      if length(exec_value) > 1 then     
          
          exec_sql := 'INSERT INTO TRANS_VEH_REPAIR(ADDWHO,ADDTIME' || exec_sql || ') VALUES(''' || IN_USERID || ''',sysdate' || exec_value || ')';
          execute immediate exec_sql;
      end if;
      OUTPUT_RESULLT := '00';
  end loop;
  close C_ACT;
  commit;
  OUTPUT_RESULLT := '00';
  delete from TMP_REPAIR_IMPORT WHERE TMPID = IN_TMPID AND USER_ID = IN_USERID;
  commit;
  EXCEPTION
    WHEN OTHERS THEN
        OUTPUT_RESULLT :='01'||sqlerrm || '[' || exec_sql || ']';
        rollback;
        --RAISE_APPLICATION_ERROR(-20101, '执行全局校验时抛出异常: '||sqlerrm, TRUE);
END;

/

--t_input_excel_verify
1	INS_TYPE	2	INS_PURCHASE_RECORD	TMP_INSUR_IMPORT	select count(1) from bas_codes where prop_code = 'INS_TYP' and name_c = '?'	AAAU/tAAEAAAYT/AAE

--t_input_excel_default
1	INS_PURCHASE_RECORD	ID	select sys_guid() from dual	1			AAAU/rAAEAAAWK9ABF
2	INS_PURCHASE_RECORD	INS_AMOUNT		3			AAAU/rAAEAAAWK9ABD
3	INS_PURCHASE_RECORD	INS_COMPANY		3			AAAU/rAAEAAAWK9ABE
4	INS_PURCHASE_RECORD	INS_DATE	select to_date('?','YYYY-MM-DD') from dual	2	INS_DATE	1	AAAU/rAAEAAAWK9ABA
5	INS_PURCHASE_RECORD	INS_NO		3			AAAU/rAAEAAAWK9ABB
6	INS_PURCHASE_RECORD	INS_TYPE	select id from bas_codes where name_c = '?' and prop_code = 'INS_TYP'	2	INS_TYPE		AAAU/rAAEAAAWK/AAi
7	INS_PURCHASE_RECORD	PLATE_NO		3			AAAU/rAAEAAAWK9ABC
8	TRANS_VEH_REPAIR	ID	select sys_guid() from dual	1			AAAU/rAAEAAAWK9AA/
9	TRANS_VEH_REPAIR	PLATE_NO		3			AAAU/rAAEAAAWK9AA7
10	TRANS_VEH_REPAIR	PRE_START_TIME	select to_date('?','YYYY-MM-DD HH24:MI:SS') from dual	2	PRE_START_TIME	1	AAAU/rAAEAAAWK9AA6
11	TRANS_VEH_REPAIR	REPAIR_ADDRESS		3			AAAU/rAAEAAAWK9AA+
12	TRANS_VEH_REPAIR	REPAIR_OBJECT		3			AAAU/rAAEAAAWK9AA8
13	TRANS_VEH_REPAIR	TOTAL_AMOUNT		3			AAAU/rAAEAAAWK9AA9
14	TRANS_VEH_VERIFY	ID	select sys_guid() from dual	1			AAAU/rAAEAAAWK9AA5
15	TRANS_VEH_VERIFY	NEXT_DATE	select to_date('?','YYYY-MM-DD') from dual	2	NEXT_DATE	1	AAAU/rAAEAAAWK9AA0
16	TRANS_VEH_VERIFY	PLATE_NO		3			AAAU/rAAEAAAWK9AA1
17	TRANS_VEH_VERIFY	SUPLR_ID	select id from bas_supplier where suplr_cname = '?'	2	SUPLR_NAME		AAAU/rAAEAAAWK9AA2
18	TRANS_VEH_VERIFY	VERIFY_ADDRESS		3			AAAU/rAAEAAAWK9AA4
19	TRANS_VEH_VERIFY	VERIFY_AMOUNT		3			AAAU/rAAEAAAWK9AA3
20	TRANS_VEH_VERIFY	VERIFY_DATE	select to_date('?','YYYY-MM-DD') from dual	2	VERIFY_DATE	1	AAAU/rAAEAAAWK9AAz

--2017-5-9 yuuanlei new version
create or replace procedure SAVE_SUPPLIER(
my_s_code varchar2,
my_s_cname varchar2,
my_s_ename varchar2,
my_s_name varchar2,
my_hint_code varchar2,
my_t_flag varchar2,
my_w_flag varchar2,
my_s_typ varchar2,
my_c_flag varchar2,
my_intl_flag varchar2,
my_bill_to varchar2,
my_property varchar2,
my_grade varchar2,
my_repesentative varchar2,
my_area_id varchar2,
my_addr varchar2,
my_zip varchar2,
my_c_name varchar2,
my_c_tel varchar2,
my_c_fax varchar2,
my_c_email varchar2,
my_url varchar2,
my_m_org_id varchar2,
my_bank varchar2,
my_acc_num varchar2,
my_taxno varchar2,
my_i_title varchar2,
my_i_deadline varchar2,
my_sett_typ varchar2,
my_pay_typ varchar2,
my_sett_cyc varchar2,
my_credit_limit varchar2,
my_currency varchar2,
my_ap_deadline varchar2,
my_whse_id varchar2,
my_eqmt_num varchar2,
my_v_for_flag varchar2,
my_i_flag varchar2,
my_ins_flag varchar2,
my_i_amt varchar2,
my_i_efct_dt varchar2,
my_i_exp_dt varchar2,
my_notes varchar2,
my_rep_til varchar2,
my_e_flag varchar2,
my_black_flag varchar2,
udf2 varchar2,
udf4 varchar2,
my_addwho varchar2,
my_org_id varchar2,
my_org_name varchar2,
out_result_code out varchar2
) is
count_ int;
sys_id varchar2(32);
sys_o_id varchar2(32);
c_serv_id varchar2(32);
sys_show_seq int;
sys_bill_to varchar2(50):=my_bill_to;--结算方
  cursor ucrr is
            select id,SRVC_NAME,default_flag from bas_trans_service where ENABLE_FLAG='Y';
begin
     out_result_code:='00';
     /*select count(1) into count_ from bas_supplier
            where suplr_code=my_s_code or suplr_cname=my_s_cname or short_name =my_s_name or hint_code = my_hint_code;
     if count_ > 0 then
        out_result_code:='02';--违反唯一性约束
        return;
     end if;*/

     select sys_guid() into sys_id from dual;
     IF my_s_name = my_bill_to then
              sys_bill_to :=sys_id;
     end if;

     insert into bas_supplier(ID,SUPLR_CODE,SUPLR_CNAME,SUPLR_ENAME,SHORT_NAME,HINT_CODE,TRANSPORT_FLAG,WAREHOUSE_FLAG,SUPLR_TYP,
                 CONTRACT_FLAG,INTL_FLAG,BILL_TO,PROPERTY,GRADE,REPRESENTATIVE,AREA_ID,ADDRESS,ZIP,CONT_NAME,CONT_TEL,
                 CONT_FAX,CONT_EMAIL,URL,MAINT_ORG_ID,SHOW_SEQ,BANK,ACC_NUM,TAXNO,INVOICE_TITLE,INV_DEADLINE,SETT_TYP,
                 PAY_TYP,SETT_CYC,CREDIT_LIMIT,CURRENCY,AP_DEADLINE,WHSE_ID,EQMT_NUM,VEHICLE_FOR_FLAG,INVOICE_FLAG,
                 INS_FLAG,INS_AMT,INS_EFCT_DT,INS_EXP_DT,NOTES,REP_TIL,ENABLE_FLAG,MODIFY_FLAG,ADDWHO,ADDTIME,udf2,udf4,Blacklist_Flag)
            values(sys_id,my_s_code,my_s_cname,my_s_ename,my_s_name,my_hint_code,my_t_flag,my_w_flag,my_s_typ,
                 my_c_flag,my_intl_flag,sys_bill_to,my_property,my_grade,my_repesentative,my_area_id,my_addr,my_zip,my_c_name,my_c_tel,
                 my_c_fax,my_c_email,my_url,my_m_org_id,sys_show_seq,my_bank,my_acc_num,my_taxno,my_i_title,my_i_deadline,my_sett_typ,
                 my_pay_typ,my_sett_cyc,my_credit_limit,my_currency,my_ap_deadline,my_whse_id,my_eqmt_num,my_v_for_flag,my_i_flag,
                 my_ins_flag,my_i_amt,to_date(my_i_efct_dt,'yyyy-mm-dd'),to_date(my_i_exp_dt,'yyyy-mm-dd'),my_notes,my_rep_til,my_e_flag,'Y',my_addwho,sysdate,udf2,udf4,my_black_flag);

      --插入默认执行机构
      select sys_guid() into sys_o_id from dual;
      insert into bas_supplier_org(id,org_id,suplr_id,org_name,default_flag,addtime,addwho)
             values (sys_o_id,my_org_id,sys_id,my_org_name,'Y',sysdate,my_addwho);

                  for coo in ucrr loop
                select sys_guid() into c_serv_id from dual;
                if coo.default_flag='Y' then --插入默认运输服务
                   begin
                       insert into bas_suplr_trans_srvc(id,trans_srvc_id,suplr_id,service_name,default_flag,addwho,addtime)
                              values(c_serv_id,coo.id,sys_id,coo.srvc_name,'Y',my_addwho,sysdate);
                   end;
                ELSE
                    begin
                        insert into bas_suplr_trans_srvc(id,trans_srvc_id,suplr_id,service_name,default_flag,addwho,addtime)
                              values(c_serv_id,coo.id,sys_id,coo.srvc_name,'N',my_addwho,sysdate);
                    end;
                END IF;
            end loop;
      out_result_code:=out_result_code || sys_id;
      commit;

      EXCEPTION
          WHEN OTHERS THEN
               ROLLBACK;
               out_result_code :=sqlcode || sqlerrm; --失败标记
               RETURN;
end;

/

create or replace view r_kpi_tmp_rate_t as
select
    t.shpm_no,t.odr_time,t.suplr_name,t.depart_time,t.arrive_whse_time,
   -- (case when load.addtime is not null and t.pre_whse_time is not null then ceil(load.addtime-t.pre_whse_time) else -1 end) as whse_delay_days,
   --yuanlei 2012-09-17
   (case when t.arrive_whse_time is not null and t.pre_whse_time is not null then ceil(t.arrive_whse_time-t.pre_whse_time) else -1 end) as whse_delay_days,
    --yuanlei
    nvl(ceil(NVL(t.depart_time,sysdate) - t.pre_load_time),-99) as send_delay_days,
    --nvl(t.UNLOAD_DELAY_DAYS, -99) as UNLOAD_DELAY_DAYS,
    nvl(ceil(NVL(t.unload_time,sysdate) - t.pre_unload_time),-99) as unload_delay_days,
    (nvl(NVL(pod_time,sysdate) - unload_time,0) - 7) as pod_delay_days
    from trans_shipment_header t
  --  (select max(addtime) addtime,doc_no from trans_customeract_log where action_typ ='UNLOAD_WHSE' group by doc_no) load
   -- where t.load_no = load.doc_no(+)
    where t.status < 90

    --yuanlei 2012-09-12 去除客户自提的数据
    and NVL(t.SLF_PICKUP_FLAG,'N') = 'N'
    and t.Suplr_Name <> '自提物流'
    --yuanlei;

/

create or replace view r_kpi_unload_rate as
select t.suplr_name,t.depart_time as addtime,
(case when unload_delay_days<=0 then 1 else 0 end) as DAYS,
1 as DAYS0,
(case when unload_delay_days > 0 and unload_delay_days <= 1 then 1 else 0 end) as DAYS1,
(case when unload_delay_days > 1 and unload_delay_days <= 2 then 1 else 0 end) as DAYS2,
(case when unload_delay_days > 2 and unload_delay_days <= 3 then 1 else 0 end) as DAYS3,
(case when unload_delay_days > 3 and unload_delay_days <= 4 then 1 else 0 end) as DAYS4,
(case when unload_delay_days > 4 and unload_delay_days <= 5 then 1 else 0 end) as DAYS5,
(case when unload_delay_days > 5 then 1 else 0 end) as DAYS6
from R_KPI_TMP_RATE_t t;

/

create or replace view v_shpm_track as
select t.custom_odr_no --承运单号
,t.load_name --出发地
,t.unload_name --到达地
,to_char(t.pre_load_time,'YYYY-MM-DD HH24:MI') as PRE_LOAD_TIME --要求到达提货点日期
,to_char(t.pre_unload_time,'YYYY-MM-DD HH24:MI') as PRE_UNLOAD_TIME --要求到达日期
,t.tot_gross_w --吨位
,t3.udf4 --开票方（简写）
,t1.suplr_name --承运商
,t1.plate_no --车牌号
,t1.driver --驾驶员姓名
,t1.mobile  --驾驶员联系方式
,t4.due_fee  --运费（含开票点）
,'GPS:' || t7.equip_no || ',温控仪:' || nvl(t5.equip_no,t6.equip_no) as EQUIPNO -- 温度记录编号（含GPS和温度记录仪）
,to_char(t1.arrive_whse_time,'YYYY-MM-DD HH24:MI') as ARRIVE_WHSE_TIME  --实际到达提货点时间
,to_char(t1.start_load_time,'YYYY-MM-DD HH24:MI')  as START_LOAD_TIME --实际装货时间
,to_char(t1.end_load_time,'YYYY-MM-DD HH24:MI') as END_LOAD_TIME --实际离开提货点时间
,t8.CURRENT_LOC --在途跟踪
,t8.TEMPERATURE --在途温度
,to_char(t1.CAST_BILL_TIME,'YYYY-MM-DD') as ARRIVE_DATE --实际到达卸货点日期
,to_char(t1.CAST_BILL_TIME,'HH24:MI:SS') as CAST_BILL_TIME --卸货投单时间
,to_char(t1.START_UNLOAD_TIME,'HH24:MI:SS') as START_UNLOAD_TIME --卸货开始时间
,to_char(t1.UNLOAD_TIME,'HH24:MI:SS') as UNLOAD_TIME --卸货结束时间
,t1.udf1 --卸货温度
,t1.track_notes --异常备注
,'' as ADDITIONAL --额外费用录入
,t.customer_id,t.odr_no,t1.shpm_no,t1.load_no,t1.exec_org_id
from trans_order_header t
,trans_shipment_header t1
,trans_load_header t2
,bas_supplier t3
,trans_bill_pay t4
,bas_tempeq t5
,bas_tempeq t6
,bas_gpseq t7
,trans_track_trace t8
where t.odr_no = t1.odr_no
and t1.load_no = t2.load_no
and t.suplr_id = t3.id(+)
and t1.load_no = t4.doc_no(+)
and t2.temp_no1 = t5.id(+)
and t2.temp_no2 = t6.id(+)
and t2.gps_no1 = t7.id(+)
and t1.shpm_no = t8.shpm_no(+)
and t1.status >= '40';

/

create or replace view lc_pay_customer_detail as
select rownum-1 as col1,'' as col2,'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '01' AS col3,
 'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '-' || t1.short_name as col4,
 'P' || to_char(last_day(add_months(sysdate,-1)),'YYYYMM') || '-' || t1.short_name as col5,t1.AP_DEADLINE || '天' as col6,'供应商' as col7,t1.suplr_cname as col8,
 '业务管理' as col9,'余磊' as col10,'长途运输成本Long-distanceTruckingCost' as col11,t3.customer_cname as col12,'人民币' as col13,'1' as col14,t.TOT_AMOUNT as col15,
 t.TOT_AMOUNT as col16,'CN03' as col17,t1.udf2 as col18,'' as col19,'' as col20,'' as col21,to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD') as col22,
 '' as col23,t.belong_month as col24,t2.short_name as col25,'' as col26,'国内采购' as col27
 from bill_pay_initdetails t
 ,bas_supplier t1
 ,BAS_CUSTOMER t2
 ,bas_customer t3
 where t.suplr_id = t1.id(+)
 and t.customer_id = t2.id(+)
 and t2.parent_customer_id = t3.id(+);

/

CREATE OR REPLACE VIEW V_PAY_INIT AS
select t.id,'扣款单' as FEE_TYPE,t.vehicle_typ_id,t2.suplr_cname,t.LOAD_NO,t.custom_odr_no,'' as load_date,'' as unload_date,t3.load_name,t3.unload_name,t4.vehicle_type,BELONG_MONTH,t1.name_c as TYPE_NAME,DEDUCT_AMOUNT AS AMOUNT,t.descr,t.plate_no
from BILL_PAY_DEDUCT t
,BAS_CODES t1
,bas_supplier t2
,trans_order_header t3
,bas_vehicle_type t4
where t.deduct_type = t1.id(+)
and t.suplr_id = t2.id
and t.custom_odr_no = t3.custom_odr_no(+)
and t3.vehicle_typ = t4.id(+)
and t.status = 'C71638421A6C450382CFF8C7C201718F'
union all
select t.id,'补贴单' as FEE_TYPE,t.vehicle_typ_id,t2.suplr_cname,t.LOAD_NO,t.custom_odr_no,'' as load_date,'' as unload_date,t3.load_name,t3.unload_name,t4.vehicle_type,BELONG_MONTH,t1.name_c as TYPE_NAME,ALLOWANCE_AMOUNT AS AMOUNT,t.descr,t.plate_no
from BILL_PAY_ALLOWANCE t
,BAS_CODES t1
,bas_supplier t2
,trans_order_header t3
,bas_vehicle_type t4
where t.allowance_type = t1.id(+)
and t.suplr_id = t2.id
and t.custom_odr_no = t3.custom_odr_no(+)
and t3.vehicle_typ = t4.id(+)
and t.status = 'C71638421A6C450382CFF8C7C201718F'
union all
select t.id,'运费' as FEE_TYPE,t.vehicle_typ_id,t.suplr_name,t.LOAD_NO,'' custom_odr_no,'' as load_date,'' as unload_date,'' as load_name,unload_name,t2.vehicle_type,t.BELONG_MONTH,'应付运费' as TYPE_NAME,TOT_AMOUNT,t.NOTES,t.plate_no
from bill_pay_initdetails t
,bas_vehicle_type t2
where t.vehicle_typ_id = t2.id(+)
and t.init_no is null
--费用类型、承运商、调度单号、客户单号、发货日期、收货日期、发货方、收货方、车型、所属期、类型说明、费用金额（含税）

--2017-05-09 yuanlei
create or replace view r_kpi_loadwh_rate as
select t.suplr_name,t.odr_time,t.arrive_whse_time as addtime,t.exec_org_id,t.exec_org_id_name,t.suplr_id,
tot_count as DAYS,
(case when whse_delay_days = 0 then 1 else 0 end) as DAYS0,
(case when whse_delay_days = 1 then 1 else 0 end) as DAYS1,
(case when whse_delay_days = 2 then 1 else 0 end) as DAYS2,
(case when whse_delay_days = 3 then 1 else 0 end) as DAYS3,
(case when whse_delay_days = 4 then 1 else 0 end) as DAYS4,
(case when whse_delay_days = 5 then 1 else 0 end) as DAYS5,
(case when whse_delay_days >= 6 then 1 else 0 end) as DAYS6
from (

select '1' as tot_count,t.shpm_no,t.odr_time,t.suplr_name,t.arrive_whse_time,t.exec_org_id,t.exec_org_id_name,t.suplr_id,
(case when t.pre_load_time is null then 0 else ceil(t.arrive_whse_time-t.pre_whse_time) end) as whse_delay_days
from trans_shipment_header t
where status<90 and t.ARRIVE_WHSE_TIME is not null

) 
t;

create or replace view r_kpi_load_rate as
select t.suplr_id,t.suplr_name,t.odr_time as addtime,t.exec_org_id,t.exec_org_id_name,
tot_count as DAYS,
1 as DAYS0,
(case when send_delay_days = 1 then 1 else 0 end) as DAYS1,
(case when send_delay_days = 2 then 1 else 0 end) as DAYS2,
(case when send_delay_days = 3 then 1 else 0 end) as DAYS3,
(case when send_delay_days = 4 then 1 else 0 end) as DAYS4,
(case when send_delay_days = 5 then 1 else 0 end) as DAYS5,
(case when send_delay_days >= 6 then 1 else 0 end) as DAYS6
from
(
select '1' as tot_count,t.exec_org_id,t.exec_org_id_name,t.shpm_no,t.odr_time,t.suplr_id,t.suplr_name,
(case when t.pre_load_time is null then 0 else ceil(t.END_LOAD_TIME-t.pre_load_time) end) as send_delay_days
from trans_shipment_header t
where t.status < 90 and t.END_LOAD_TIME is not null
) 
t;

create or replace view r_kpi_unload_rate as
select t.suplr_id,t.suplr_name,t.odr_time as addtime,t.exec_org_id,t.exec_org_id_name,
tot_count as DAYS,
1 as DAYS0,
(case when unload_delay_days = 1 then 1 else 0 end) as DAYS1,
(case when unload_delay_days = 2 then 1 else 0 end) as DAYS2,
(case when unload_delay_days = 3 then 1 else 0 end) as DAYS3,
(case when unload_delay_days = 4 then 1 else 0 end) as DAYS4,
(case when unload_delay_days = 5 then 1 else 0 end) as DAYS5,
(case when unload_delay_days >= 6 then 1 else 0 end) as DAYS6
from
(
select '1' as tot_count,t.exec_org_id,t.exec_org_id_name,t.shpm_no,t.odr_time,t.suplr_id,t.suplr_name,
(case when t.PRE_UNLOAD_TIME is null then 0 else ceil(t.CAST_BILL_TIME-t.PRE_UNLOAD_TIME) end) as unload_delay_days
from trans_shipment_header t
where t.status < 90 and t.CAST_BILL_TIME is not null
) 
t;


create or replace view r_kpi_pod_rate as
select t.suplr_id,t.suplr_name,t.odr_time as addtime,t.exec_org_id,t.exec_org_id_name,
tot_count as DAYS,
1 as DAYS0,
(case when pod_delay_days = 1 then 1 else 0 end) as DAYS1,
(case when pod_delay_days = 2 then 1 else 0 end) as DAYS2,
(case when pod_delay_days = 3 then 1 else 0 end) as DAYS3,
(case when pod_delay_days = 4 then 1 else 0 end) as DAYS4,
(case when pod_delay_days = 5 then 1 else 0 end) as DAYS5,
(case when pod_delay_days >= 6 then 1 else 0 end) as DAYS6
from
(
select '1' as tot_count,t.exec_org_id,t.exec_org_id_name,t.shpm_no,t.odr_time,t.suplr_id,t.suplr_name,
(case when t.PRE_POD_TIME is null then 0 else ceil(t.POD_TIME-t.PRE_POD_TIME) end) as pod_delay_days
from trans_shipment_header t
where t.status < 90 and t.POD_TIME is not null
) 
t;

create or replace view r_kpi_order as
select t.EXEC_ORG_ID,t.EXEC_ORG_ID_NAME,t.CUSTOMER_ID,t.ADDTIME,t.ODR_TIME,t.ODR_NO,t.REFENENCE1,t.CUSTOMER_NAME,t.BIZ_TYP,t.STATUS_NAME,
t.LOAD_AREA_ID2,t.LOAD_AREA_NAME2,t.LOAD_ADDRESS,t.UNLOAD_AREA_ID2,t.UNLOAD_AREA_NAME2,t.UNLOAD_ADDRESS,t.UNLOAD_CONTACT,t.UNLOAD_TEL
--,(case when AUDIT_TIME is null or PRE_AUDIT_TIME >= AUDIT_TIME then 'Y' else 'N' end) AUDIT_FLAG
,(case when DEPART_TIME is null or PRE_LOAD_TIME >= DEPART_TIME then 'Y' else 'N' end) LOAD_FLAG
,(case when UNLOAD_TIME is null or PRE_UNLOAD_TIME >= UNLOAD_TIME then 'Y' else 'N' end) UNLOAD_FLAG
,(case when POD_TIME is null or PRE_POD_TIME >= POD_TIME then 'Y' else 'N' end) POD_FLAG
--,to_char(t.PRE_AUDIT_TIME,'YYYY-MM-DD HH24:MI') AS PRE_AUDIT_TIME
--,to_char(t.AUDIT_TIME,'YYYY-MM-DD HH24:MI') AS AUDIT_TIME
,to_char(t.PRE_LOAD_TIME,'YYYY-MM-DD HH24:MI') AS PRE_LOAD_TIME
,to_char(t.DEPART_TIME,'YYYY-MM-DD HH24:MI') AS LOAD_TIME
,to_char(t.PRE_UNLOAD_TIME,'YYYY-MM-DD HH24:MI') AS PRE_UNLOAD_TIME
,to_char(t.UNLOAD_TIME,'YYYY-MM-DD HH24:MI') AS UNLOAD_TIME
,to_char(t.PRE_POD_TIME,'YYYY-MM-DD HH24:MI') AS PRE_POD_TIME
,to_char(t.POD_TIME,'YYYY-MM-DD HH24:MI') AS POD_TIME
,t1.name_c as BIZ_TYP_NAME
--,(case when AUDIT_TIME is null or PRE_AUDIT_TIME is null then 0 else ceil((AUDIT_TIME-PRE_AUDIT_TIME)*24) end) AUDIT_HOURS
,(case when END_LOAD_TIME is null or PRE_LOAD_TIME is null then 0 else ceil((END_LOAD_TIME-PRE_LOAD_TIME)*24) end) LOAD_HOURS
,(case when CAST_BILL_TIME is null or PRE_UNLOAD_TIME is null then 0 else ceil((CAST_BILL_TIME-PRE_UNLOAD_TIME)*24) end) UNLOAD_HOURS
,(case when POD_TIME is null or PRE_POD_TIME is null then 0 else ceil((POD_TIME-PRE_POD_TIME)*24) end) POD_HOURS
,to_char(t.ARRIVE_WHSE_TIME,'YYYY-MM-DD HH24:MI') as ARRIVE_WHSE_TIME
,to_char(t.START_LOAD_TIME,'YYYY-MM-DD HH24:MI') as START_LOAD_TIME
,to_char(t.END_LOAD_TIME,'YYYY-MM-DD HH24:MI') as END_LOAD_TIME
,(case when ARRIVE_WHSE_TIME is null or START_LOAD_TIME is null then 0 else ceil((START_LOAD_TIME-ARRIVE_WHSE_TIME)*24) end) LOAD_WAIT_HOURS
,(case when END_LOAD_TIME is null or START_LOAD_TIME is null then 0 else ceil((END_LOAD_TIME-START_LOAD_TIME)*24) end) PICKLOAD_HOURS
,to_char(t.CAST_BILL_TIME,'YYYY-MM-DD HH24:MI') as CAST_BILL_TIME
,to_char(t.START_UNLOAD_TIME,'YYYY-MM-DD HH24:MI') as START_UNLOAD_TIME
,(case when START_UNLOAD_TIME is null or CAST_BILL_TIME is null then 0 else ceil((START_UNLOAD_TIME-CAST_BILL_TIME)*24) end) UNLOAD_WAIT_HOURS
,(case when UNLOAD_TIME is null or START_UNLOAD_TIME is null then 0 else ceil((UNLOAD_TIME-START_UNLOAD_TIME)*24) end) UNLOADING_HOURS
FROM TRANS_SHIPMENT_HEADER T
,BAS_CODES t1
where t.BIZ_TYP = t1.id(+);

--yuanlei 2017-05-11
CREATE OR REPLACE VIEW V_PAY_INIT AS
select t.id,'扣款单' as FEE_TYPE,t.vehicle_typ_id,t2.suplr_cname,t.LOAD_NO,t.custom_odr_no,'' as load_date,'' as unload_date,'' as load_name,'' as unload_name,t4.vehicle_type,BELONG_MONTH,t1.name_c as TYPE_NAME,DEDUCT_AMOUNT AS AMOUNT,t.descr,t.plate_no
from BILL_PAY_DEDUCT t
,BAS_CODES t1
,bas_supplier t2
,bas_vehicle_type t4
where t.deduct_type = t1.id(+)
and t.suplr_id = t2.id
and t.vehicle_typ_id = t4.id(+)
and t.status = 'C71638421A6C450382CFF8C7C201718F'
and t.init_no = 'X'
union all
select t.id,'补贴单' as FEE_TYPE,t.vehicle_typ_id,t2.suplr_cname,t.LOAD_NO,t.custom_odr_no,'' as load_date,'' as unload_date,'' as load_name,'' as unload_name,t4.vehicle_type,BELONG_MONTH,t1.name_c as TYPE_NAME,ALLOWANCE_AMOUNT AS AMOUNT,t.descr,t.plate_no
from BILL_PAY_ALLOWANCE t
,BAS_CODES t1
,bas_supplier t2
,bas_vehicle_type t4
where t.allowance_type = t1.id(+)
and t.suplr_id = t2.id
and t.vehicle_typ_id = t4.id(+)
and t.status = 'C71638421A6C450382CFF8C7C201718F'
and t.init_no = 'X'
union all
select t.id,'运费' as FEE_TYPE,t.vehicle_typ_id,t.suplr_name,t.LOAD_NO,'' custom_odr_no,'' as load_date,'' as unload_date,'' as load_name,unload_name,t2.vehicle_type,t.BELONG_MONTH,'应付运费' as TYPE_NAME,TOT_AMOUNT,t.NOTES,t.plate_no
from bill_pay_initdetails t
,bas_vehicle_type t2
where t.vehicle_typ_id = t2.id(+)
and t.init_no  = 'X'
--费用类型、承运商、调度单号、客户单号、发货日期、收货日期、发货方、收货方、车型、所属期、类型说明、费用金额（含税）

CREATE OR REPLACE PROCEDURE SP_SHPM_LOAD_FINISH (
in_load_no VARCHAR2,            --调度单号
in_shpm_no LST,                  --作业单号
in_ARRIVER_WHSE_TIME VARCHAR2,   --到库时间
in_START_LOAD_TIME VARCHAR2,     --开始装货时间
in_END_LOAD_TIME VARCHAR2,       --完成装货时间
in_udf3 VARCHAR2,               --开门温度
in_udf4 VARCHAR2,               --关门温度
in_veh_pos number,              --车位
in_LOAD_NOTES VARCHAR2,         --备注
in_SHPM_ROW LST,               --明细行号
in_LD_QNTY LST,                --发货数量
in_UNLD_QNTY LST,              --收货数量
in_UNLD_VOL LST,               --收货体积
in_UNLD_GWGT LST,              --收货重量
in_user_id VARCHAR2,           --用户ID
output_result OUT VARCHAR2
)
IS
/**
 *  yuanlei 2011-1-1
 *  V1.1 编辑时间 ：2010-2-16 添加司机服务态度、满意度
 *  V1.2 编辑时间 ：2011-9-20 添加了作业单跟踪记录字段TRACK_NOTES，自动添加跟踪记录时，更新作业单该字段值，避免关联查询
 */
/*in_shpm_no LST := LST();
in_SHPM_ROW LST := LST();
in_LD_QNTY LST := LST();
in_UNLD_QNTY LST := LST();
in_UNLD_VOL LST := LST();
in_UNLD_GWGT LST := LST();
in_UNLD_NWGT LST := LST();
in_UNLD_WORTH LST := LST();*/
op_customer_id varchar(32);
op_exec_org_id varchar(32);
op_odr_no varchar(50);
op_trans_uom varchar(32);
op_price number(18,8);
op_suplr_id varchar(32);
op_plate_no varchar(20);
op_driver varchar(20);
op_mobile varchar(20);
op_trans_srvc_id varchar2(32);
op_biz_typ varchar2(32);
op_last_jrny_no varchar2(100);
op_exec_org_id_name varchar2(50);
t_sub_qnty number(18,8);
t_unload_days number(4);
op_status varchar2(50);
op_arrive_time varchar2(50);
op_start_time varchar2(30);
op_end_time varchar2(30);

op_qnty number(18,8);
op_vol number(18,8);
op_gwgt number(18,8);
op_nwgt number(18,8);
op_worth number(18,8);

tmp_shpm_no varchar(100);
tmp_abnormal_notes varchar(512);
t_count number(4);
--in_SERVICE_CODE varchar(512);
--in_SATISFY_CODE varchar(512);
t_status varchar(50);

in_odr_no LST := LST();

op_stat_code varchar2(50);
op_sysid varchar2(32);
op_temperature_name varchar2(50);
op_addr_code varchar2(100);

CURSOR head IS
      SELECT SHPM_NO,refenence1,odr_no,tot_qnty,refenence4,plate_no,udf3,load_name,load_id,load_address,unload_name,end_contact,customer_name,biz_typ,trans_srvc_id FROM TRANS_SHIPMENT_HEADER WHERE LOAD_NO = in_load_no;

op_notes_name varchar2(400);
op_notes_code varchar2(50);
BEGIN
    output_result := '00';
    if length(in_ARRIVER_WHSE_TIME) > 19 then
        op_arrive_time := substr(in_ARRIVER_WHSE_TIME,0,18);
    else
        op_arrive_time := in_ARRIVER_WHSE_TIME;
    end if;
    if length(in_START_LOAD_TIME) > 19 then
        op_start_time := substr(in_START_LOAD_TIME,0,18);
    else
        op_start_time := in_START_LOAD_TIME;
    end if;
    if length(in_END_LOAD_TIME) > 19 then
        op_end_time := substr(in_END_LOAD_TIME,0,18);
    else
        op_end_time := in_END_LOAD_TIME;
    end if;
    --in_SERVICE_CODE := '';
    --in_SATISFY_CODE := '';
    /*in_shpm_no.extend;
    in_shpm_no(1) := null;
    in_SHPM_ROW.extend;
    in_SHPM_ROW(1) := ' ';
    in_LD_QNTY.extend;
    in_LD_QNTY(1) := '';
    --in_LD_QNTY.extend;
    --in_LD_QNTY(2) := '12000';
    in_UNLD_QNTY.extend;
    in_UNLD_QNTY(1) := '120';
    --in_UNLD_QNTY.extend;
    --in_UNLD_QNTY(2) := '12000';
    in_UNLD_VOL.extend;
    in_UNLD_VOL(1) := '26822600';
    --in_UNLD_VOL.extend;
    --in_UNLD_VOL(2) := '10729040';
    in_UNLD_GWGT.extend;
    in_UNLD_GWGT(1) := '46';
    --in_UNLD_GWGT.extend;
    --in_UNLD_GWGT(2) := '48.4';
    in_UNLD_NWGT.extend;
    in_UNLD_NWGT(1) := '0';
    --in_UNLD_NWGT.extend;
    --in_UNLD_NWGT(2) := '0';
    in_UNLD_WORTH.extend;
    in_UNLD_WORTH(1) := '0';
    --in_UNLD_WORTH.extend;
    --in_UNLD_WORTH(2) := '0';*/
    --dbms_output.put_line('start:' || to_char(sysdate,'yyyy-mm-dd hh:mi:ss'));
    if in_load_no <> 'X' then
      select dispatch_stat into t_status from trans_load_header where load_no = in_load_no;
      if t_status <> RDPG_STAT.LOAD_AUDIT then
           output_result := '01调度单未配车审核，不能执行做装车确认!';
           return;
      end if;
      select suplr_id,exec_org_id,plate_no,driver1,mobile1 into op_suplr_id,op_exec_org_id,op_plate_no,op_driver,op_mobile from TRANS_LOAD_HEADER WHERE LOAD_NO = in_load_no;
    end if;
    if trim(in_shpm_no(1)) is not null then

        for   i   in   1..in_shpm_no.count   loop
            tmp_shpm_no := in_shpm_no(i);
            select odr_no,trans_uom,customer_id,abnomal_notes,status,biz_typ,trans_srvc_id,exec_org_id,exec_org_id_name,last_jrny_no
              into op_odr_no,op_trans_uom,op_customer_id,tmp_abnormal_notes,op_status,op_biz_typ,op_trans_srvc_id,op_exec_org_id,op_exec_org_id_name,op_last_jrny_no
              from TRANS_SHIPMENT_HEADER where shpm_no = tmp_shpm_no;
            if op_status >= '30' then
                in_odr_no.extend;
                in_odr_no(i) := op_odr_no;
                if trim(in_SHPM_ROW(1)) is not null then   --完好发货
                begin
                    for j in 1..in_SHPM_ROW.count loop
                        select qnty,vol,g_wgt,n_wgt,worth into op_qnty,op_vol,op_gwgt,op_nwgt,op_worth from TRANS_SHIPMENT_ITEM where shpm_no = tmp_shpm_no and shpm_row = in_SHPM_ROW(j);
                        --op_vol := in_LD_QNTY(j) * op_vol / op_qnty;
                        --op_gwgt := in_LD_QNTY(j) * op_gwgt / op_qnty;
                        op_vol := in_UNLD_VOL(j);
                        op_gwgt := in_UNLD_GWGT(j);
                        op_nwgt := in_UNLD_QNTY(j) * op_nwgt / op_qnty;
                        op_worth := in_UNLD_QNTY(j) * op_worth / op_qnty;
                        update TRANS_SHIPMENT_ITEM SET LD_QNTY = in_UNLD_QNTY(j),LD_VOL = op_vol,LD_GWGT = op_gwgt,LD_NWGT = op_nwgt,
                            LD_WORTH = op_worth where shpm_no = tmp_shpm_no and shpm_row = in_SHPM_ROW(j);
                        t_sub_qnty := op_qnty - in_UNLD_QNTY(j);
                        if t_sub_qnty > 0 then

                            --插入货差货损记录
                            insert into TRANS_LOSS_DAMAGE(ID,Load_No,SHPM_NO,ODR_NO,SHPM_ROW,SKU_ID,SKU_SPEC,LOTID,PACK_ID,TRANS_UOM,QNTY,
                            AMOUNT,LOSS_DAMAGE_TYP,REASON,DUTYER,CUSTOMER_ID,SUPLR_ID,EXEC_ORG_ID,NOTES,Addtime,Addwho)
                            select sys_guid(),in_load_no,in_shpm_no(i),op_odr_no,in_SHPM_ROW(j),SKU_ID,SKU_SPEC,LOT_ID,PACK_ID,op_trans_uom,t_sub_qnty,
                            op_price * t_sub_qnty,null,null,null,op_customer_id,op_suplr_id,op_exec_org_id,'',sysdate,in_user_id
                              from TRANS_SHIPMENT_ITEM where shpm_no = tmp_shpm_no and shpm_row = in_shpm_row(j);
                            output_result := output_result || 'Y';
                        else
                            output_result := output_result || 'N';
                        end if;

                        --判断是否是提货段，如果提货段数量有差异要回写到后面的段和托运单上
                        --if op_trans_srvc_id = '1' then--提货
                            update TRANS_ORDER_ITEM set QNTY = in_UNLD_QNTY(j),VOL = op_vol, G_WGT = op_gwgt, N_WGT = op_nwgt, WORTH = op_worth
                            where odr_no = op_odr_no and odr_row = in_SHPM_ROW(j);
                            if op_last_jrny_no is not null then  --行程拆分
                                update TRANS_SHIPMENT_ITEM set QNTY = in_UNLD_QNTY(j),VOL = op_vol, G_WGT = op_gwgt, N_WGT = op_nwgt, WORTH = op_worth
                                where odr_no = op_odr_no  and shpm_no != tmp_shpm_no and shpm_row = in_shpm_row(j);
                            end if;
                        --end if;

                    end loop;
                end;
                end if;
                update TRANS_SHIPMENT_HEADER SET LOAD_STAT = RDPG_STAT.LOADED_NAME, START_LOAD_TIME = to_date(op_start_time,'yyyy-MM-dd HH24:mi:ss')
                ,END_LOAD_TIME = to_date(op_end_time,'yyyy-MM-dd HH24:mi:ss'),LOAD_NOTES = in_LOAD_NOTES,UDF3 = in_udf3,veh_pos = in_veh_pos
                ,ARRIVE_WHSE_TIME = to_date(op_arrive_time,'yyyy-MM-dd HH24:mi:ss')
                --,sign_org_id = op_exec_org_id,sign_org_id_name = op_exec_org_id_name
                WHERE shpm_no = tmp_shpm_no;
                --自动给每个作业单写笔跟踪记录
                INSERT INTO TRANS_TRACK_TRACE(ID,LOAD_NO,SHPM_NO,ODR_NO,PLATE_NO,DRIVER,MOBILE,TRACER,EXEC_ORG_ID,TRACE_TIME,
                   PRE_UNLOAD_TIME,CURRENT_LOC,INFORMATION,ADDWHO,ADDTIME,notes)
                select sys_guid(),LOAD_NO,SHPM_NO,ODR_NO,op_plate_no,op_driver,to_char(op_mobile),in_user_id,op_exec_org_id,sysdate,
                   PRE_UNLOAD_TIME,UNLOAD_AREA_NAME,'装车确认',in_user_id,sysdate,in_LOAD_NOTES
                 from TRANS_SHIPMENT_HEADER where shpm_no = tmp_shpm_no;

                --实际收款金额回写托运单和作业单
                /*if in_udf4 is not null and length(trim(in_udf4)) > 0 then
                    update trans_bill_rece set PAY_FEE = in_udf4 where doc_no = op_odr_no and fee_name = '应收运费';

                end if;*/

                select UNLOAD_DELAY_DAYS into t_unload_days from trans_shipment_header where shpm_no = tmp_shpm_no;
                --插入业务日志
                insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
                    values(sys_guid(),RDPG_STAT.UNLOAD,RDPG_STAT.SHPM_NO,tmp_shpm_no,'作业单装车确认',sysdate,in_user_id);
                SP_SFSTATUS_LOG(op_biz_typ,op_trans_srvc_id,in_load_no,tmp_shpm_no,'45',in_user_id);

                --插入作业单客户日志
                --SP_CUSTOMACT_LOG('LOADED',RDPG_STAT.SHPM_NO,tmp_shpm_no,in_user_id,to_date(in_UNLOAD_TIME,'yyyy-MM-dd HH24:mi:ss') , output_result);
            end if;
        end loop;

        select count(1) into t_count from trans_shipment_header where load_no = in_load_no and load_stat <> RDPG_STAT.LOADED_NAME;
        if t_count > 0 then
            update trans_load_header set load_stat = '15',START_LOAD_TIME = to_date(op_start_time,'yyyy-MM-dd HH24:mi:ss')
              ,END_LOAD_TIME = to_date(op_end_time,'yyyy-MM-dd HH24:mi:ss'),ARRIVE_WHSE_TIME = to_date(op_arrive_time,'yyyy-MM-dd HH24:mi:ss')
            where load_no = in_load_no;
        else
            update trans_load_header set load_stat = '20',START_LOAD_TIME = to_date(op_start_time,'yyyy-MM-dd HH24:mi:ss')
              ,END_LOAD_TIME = to_date(op_end_time,'yyyy-MM-dd HH24:mi:ss'),ARRIVE_WHSE_TIME = to_date(op_arrive_time,'yyyy-MM-dd HH24:mi:ss')
            where load_no = in_load_no;
        end if;
    else
        update TRANS_SHIPMENT_HEADER SET LOAD_STAT = RDPG_STAT.LOADED_NAME, START_LOAD_TIME = to_date(op_start_time,'yyyy-MM-dd HH24:mi:ss')
        ,END_LOAD_TIME = to_date(op_end_time,'yyyy-MM-dd HH24:mi:ss'),LOAD_NOTES = in_LOAD_NOTES,UDF3 = in_udf3, UDF4 = in_udf4,veh_pos = in_veh_pos
        ,ARRIVE_WHSE_TIME = to_date(op_arrive_time,'yyyy-MM-dd HH24:mi:ss')
        WHERE load_no = in_load_no;

        UPDATE TRANS_LOAD_HEADER SET LOAD_STAT = '20' where LOAD_NO = in_load_no;

        --自动给每个作业单写笔跟踪记录
        INSERT INTO TRANS_TRACK_TRACE(ID,LOAD_NO,SHPM_NO,ODR_NO,PLATE_NO,DRIVER,MOBILE,TRACER,EXEC_ORG_ID,TRACE_TIME,
           PRE_UNLOAD_TIME,CURRENT_LOC,INFORMATION,ADDWHO,ADDTIME,notes)
        select sys_guid(),LOAD_NO,SHPM_NO,ODR_NO,op_plate_no,op_driver,to_char(op_mobile),in_user_id,op_exec_org_id,sysdate,
           PRE_UNLOAD_TIME,UNLOAD_AREA_NAME,'装车确认',in_user_id,sysdate,in_LOAD_NOTES
         from TRANS_SHIPMENT_HEADER where load_no = in_load_no;

        --插入业务日志
        insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
            values(sys_guid(),RDPG_STAT.UNLOAD,RDPG_STAT.LOAD_NO,in_load_no,'调度单装车确认',sysdate,in_user_id);
        select shpm_no,biz_typ,trans_srvc_id into tmp_shpm_no,op_biz_typ,op_trans_srvc_id from trans_shipment_header where load_no = in_load_no and rownum <= 1;
        SP_SFSTATUS_LOG(op_biz_typ,op_trans_srvc_id,in_load_no,tmp_shpm_no,'45',in_user_id);

        for it in head loop
            if tmp_shpm_no != it.shpm_no then
                begin
                    --批量复制运单状态日志
                    select notes_code,stat_code into op_notes_code,op_stat_code from sf_status_config where biz_typ = it.biz_typ and trans_srvc_id = it.trans_srvc_id and node = '45' and enable_flag = 'Y';
                    select name_c into op_notes_name from bas_codes where code = op_notes_code and prop_code = 'SF_NOTES';

                    if instr(op_notes_name,'X1') > 0 then
                        if it.load_name is null then
                            op_notes_name := replace(op_notes_name,'X1',it.load_address);
                        else
                            op_notes_name := replace(op_notes_name,'X1',it.load_name);
                        end if;
                    end if;
                    if instr(op_notes_name,'X2') > 0 then
                        op_notes_name := replace(op_notes_name,'X2',it.unload_name);
                    end if;
                    insert into sf_status_log(id,refenence1,odr_no,status,qnty,temperature,plate_no,load_no,addtime,addwho,node,shpm_no,notes,descr)
                    values(sys_guid(),it.refenence1,it.odr_no,op_stat_code,it.tot_qnty,it.refenence4,it.plate_no,in_load_no,sysdate,in_user_id,'45',it.shpm_no,op_notes_name,'装车温度:' || it.udf3 || ',数量:' || it.tot_qnty);
                exception when OTHERS THEN
                    output_result := output_result;
                end;
            end if;
        end loop;
    end if;

    commit;
    --dbms_output.put_line('end:' || to_char(sysdate,'yyyy-mm-dd hh:mi:ss'));
    EXCEPTION
    WHEN OTHERS THEN
         rollback;
         output_result:='01'||sqlerrm;
    return;
END;
/

CREATE OR REPLACE FUNCTION IS_DATE(parameter VARCHAR2) RETURN NUMBER IS
  val DATE;
BEGIN
  val := TO_DATE(NVL(parameter, 'a'), 'yyyy-mm-dd hh24:mi:ss');
  RETURN 1;
EXCEPTION
  WHEN OTHERS THEN
    RETURN 0;
END;
/

CREATE OR REPLACE FUNCTION IS_DATE(parameter VARCHAR2) RETURN NUMBER IS
  val DATE;
BEGIN
  val := TO_DATE(NVL(parameter, 'a'), 'yyyy-mm-dd hh24:mi:ss');
  RETURN 1;
EXCEPTION
  WHEN OTHERS THEN
    RETURN 0;
END;
/

CREATE OR REPLACE PROCEDURE BMS_REC_CREATE_ADJNO
(
in_INIT_NO LST,   --期初单号
in_user_id varchar2,
output_result out varchar2
)
IS
/**
 * 生成调整账单
 */
r_init_no varchar2(100);
r_adj_no varchar2(100);


op_buss_id varchar2(32);
op_buss_name varchar2(100);
op_belong_month varchar2(50);
op_initital_amount NUMBER(18,8);
op_init_amount number(18,8);
op_tax_amount NUMBER(18,8);
op_subtax_amount number(18,8);
op_adj_amount number(18,8);
op_confirm_amount number(18,8);
op_account_stat varchar2(32);
op_tax number(4,2);
op_hold_flag char(1);
op_invoice_title varchar2(50);

t_count number(4);

--in_INIT_NO LST := LST();
begin
    --in_INIT_NO.Extend;
    --in_INIT_NO(1) := '2017032700005';
    output_result := '00';
    for   i   in   1..in_INIT_NO.count loop
        r_init_no := in_INIT_NO(i);
        select buss_id,buss_name,belong_month,initital_amount,adj_amount,confirm_amount,tax_amount,subtax_amount,account_stat,init_amount,hold_flag
        into op_buss_id,op_buss_name,op_belong_month,op_initital_amount,op_adj_amount,op_confirm_amount,op_tax_amount,op_subtax_amount,op_account_stat,op_init_amount,op_hold_flag
            from bill_rec_initial WHERE INIT_NO = r_init_no;
        if op_account_stat = '20' then
            output_result :='01对账单已确认无法生成调整单!';
            return;
        end if;
        if op_hold_flag = 'Y' then
            output_result :='01对账单已生成调整单,不能重复生成!';
            return;
        end if;
        if op_adj_amount = 0 then
            output_result :='01金额未调整,不需要生成调整账单!';
            return;
        end if;
        select udf2,invoice_title into op_tax,op_invoice_title from bas_customer where id = op_buss_id;
        if op_tax is null then
            op_tax := 0;
        end if;
        SP_GET_IDSEQ('RECE_ADJNO',r_adj_no);

        /*begin
            select min(show_seq) into t_show_seq from SYS_APPROVE_SET where doc_no = 'REC_ADJNO';
        exception when no_data_found then
            output_result :='01应收调整单未设置审批流程，请先设置审批流程!';
            return;
        end;
        select ROLE_ID into op_role_id from SYS_APPROVE_SET where DOC_NO = 'REC_ADJNO' and show_seq = t_show_seq;*/
        select count(1) into t_count from BILL_REC_ADJUST where init_no = r_init_no;

        op_tax_amount := round(op_adj_amount * op_tax /(100+op_tax),2);
        insert into BILL_REC_ADJUST(id,adj_no,buss_id,buss_name,belong_month,INITITAL_AMOUNT,CONFIRM_AMOUNT,ADJ_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,status,addtime,addwho,init_no,bill_to)
        values(sys_guid(),r_adj_no,op_buss_id,op_buss_name,op_belong_month,op_initital_amount,op_confirm_amount,op_adj_amount,op_tax_amount,op_adj_amount-op_tax_amount,'10',sysdate,in_user_id,r_init_no,op_invoice_title);

        if t_count = 0 then
            insert into BILL_REC_ADJDETAILS(ID,ADJ_NO,Buss_Id,buss_name,Odr_No,Custom_Odr_No,vehicle_typ_id,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,Notes,init_amount,initital_amount,confirm_amount1,adj_amount1,addwho,addtime,init_no,init_detail_id)
            select sys_guid(),r_adj_no,buss_id,buss_name,odr_no,custom_odr_no,vehicle_typ_id,load_date,unload_date,load_name,unload_name,notes,init_amount,TOT_AMOUNT,confirm_amount,adj_amount,in_user_id,sysdate,r_init_no,id from BILL_REC_INITDETAILS
            where init_no = r_init_no;
        elsif t_count = 1 then
            update BILL_REC_ADJDETAILS set (INITITAL_AMOUNT,Confirm_amount2,ADJ_AMOUNT2) = (select tot_amount,Confirm_amount,adj_amount from BILL_REC_INITDETAILS where BILL_REC_ADJDETAILS.Init_Detail_Id = BILL_REC_INITDETAILS.ID);
        elsif t_count = 2 then
            update BILL_REC_ADJDETAILS set (INITITAL_AMOUNT,Confirm_amount3,ADJ_AMOUNT3) = (select tot_amount,Confirm_amount,adj_amount from BILL_REC_INITDETAILS where BILL_REC_ADJDETAILS.Init_Detail_Id = BILL_REC_INITDETAILS.ID);
        else
            output_result :='01最多只允许调整3次账单!';
            return;
        end if;
        update bill_rec_initial set HOLD_FLAG = 'Y' where INIT_NO = r_init_no;
    end loop;
    commit;
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result := '01' || sqlerrm;
     return;
end;
/

CREATE OR REPLACE VIEW V_REC_INVOICEINFO AS
SELECT b.act_amount,b.addtime,b.addwho,b.amount,b.bank_account,b.bank_point,b.bill_to,b.edittime,
       b.editwho,b.fee_name,b.id,b.invoice_address,b.invoice_by,b.invoice_no,b.invoice_num,
       to_char(b.invoice_time,'yyyy/mm/dd HH:mm')as invoice_time,b.invoice_type,b.notes,b.rece_amount,b.rece_by,
       to_char(b.rece_time,'yyyy/mm/dd HH:mm')as rece_time,b.tax_amount,b.tax_no,b.tax_ratio

FROM
    BILL_REC_INVOICEINFO b;
    
create or replace procedure SP_SHPM_CANCELRECLAIM(
in_shpm_no LST,                  --托运单号
in_user_id VARCHAR2,           --用户ID
output_result OUT VARCHAR2
)
IS
/*in_odr_no LST := LST();*/
t_all_shpm_no varchar(400);
t_count number(8);
t_sql varchar(1024);
op_shpm_no varchar2(100);
op_odr_no varchar(50);
op_status varchar(32);
op_status_name varchar(50);
op_load_no varchar2(100);
op_account_stat varchar2(100);
tmp_shpm_no varchar(400);  --yuanlei 2012-09-13 增加临时参数
BEGIN
    output_result := '00';
    /*in_odr_no.extend;
    in_odr_no(1) := 'YH1101069559';*/
    for   i   in   1..in_shpm_no.count   loop
        op_shpm_no := in_shpm_no(i);
        t_all_shpm_no := t_all_shpm_no || ',''' || op_shpm_no || '''';
        select odr_no,load_no into op_odr_no,op_load_no from TRANS_SHIPMENT_HEADER where shpm_no = op_shpm_no;
        select account_stat into op_account_stat from trans_load_header where load_no = op_load_no;
        if op_account_stat is not null and op_account_stat = '20' then
            output_result := '01调度单[' || op_load_no || ']已对账,无法取消回单';
            rollback;
            return; 
        end if;
        --清除托运单的实际回单时间、回单延迟原因，实际到货时间、到货延迟原因，货损货差标记，状态
        update TRANS_SHIPMENT_HEADER SET STATUS = RDPG_STAT.SHPM_UNLOAD,STATUS_NAME = RDPG_STAT.SHPM_UNLOAD_NAME ,POD_TIME = null, POD_DELAY_REASON = null,OP_POD_TIME = null,POD_DELAY_DAYS = null
          WHERE shpm_no = in_shpm_no(i);
        update TRANS_BILL_DETAIL SET STATUS = RDPG_STAT.SHPM_UNLOAD WHERE DOC_NO = in_shpm_no(i);
        --清除收货数量、毛重、体积、净重、货值
        --update TRANS_ORDER_ITEM SET UNLD_QNTY = null,UNLD_VOL = null,UNLD_GWGT = null,UNLD_NWGT = null,UNLD_WORTH = null
        --  where odr_no = in_odr_no(i);
        --删除货差货损记录
        delete from TRANS_LOSS_DAMAGE where SHPM_NO = in_shpm_no(i);
        --插入业务日志
        insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
            values(sys_guid(),RDPG_STAT.RECLAIM,RDPG_STAT.SHPM_NO,in_shpm_no(i),'',sysdate,in_user_id);
        delete from sf_status_log where status = '090' and shpm_no = op_odr_no;
        --yuanlei 2012-09-13 解决批量取消回单的BUG
        /*t_all_shpm_no :=  substr(t_all_shpm_no,2,length(t_all_shpm_no) - 1);
        --判断作业单所在原始托运单的其他子作业单状态
        t_sql := 'SELECT COUNT(1) FROM TRANS_SHIPMENT_HEADER WHERE odr_no = '''||op_odr_no||''' AND status>= ' || RDPG_STAT.SHPM_RECEIPT || '
              AND SHPM_NO not in (' || t_all_shpm_no || ')';
        */
        tmp_shpm_no :=  substr(t_all_shpm_no,2,length(t_all_shpm_no) - 1);
        --判断作业单所在原始托运单的其他子作业单状态
        t_sql := 'SELECT COUNT(1) FROM TRANS_SHIPMENT_HEADER WHERE odr_no = '''||op_odr_no||''' AND status>= ' || RDPG_STAT.SHPM_RECEIPT || '
              AND SHPM_NO not in (' || tmp_shpm_no || ')';
        --yuanlei
        execute   immediate t_sql into t_count;
        if t_count > 0 then
            op_status := RDPG_STAT.SO_PART_RECEIPT;
            op_status_name := RDPG_STAT.SO_PART_RECEIPT_NAME;
        else
            op_status := RDPG_STAT.SO_CONFIRM;
            op_status_name := RDPG_STAT.SO_CONFIRM_NAME;
        end if;
        update TRANS_ORDER_HEADER SET status = op_status,status_name = op_status_name,OP_POD_TIME = null,POD_TIME = null,POD_DELAY_REASON = null,POD_SIGNATARY = null,POD_ORG_ID = null WHERE ODR_NO = op_odr_no;
    end loop;
    commit;
    EXCEPTION
    WHEN OTHERS THEN
         rollback;
         output_result:='01'||sqlerrm;
    return;
END;
/

--caijiante  2017-05-11
--SYS_FUNC_PAGE加一条记录
962?	22963	P06_T032_P1_04	P06_T032_P1	保存	150	Y	2015/5/27	wpsadmin	保存	T	


--yuanlei 2017-05-12
alter table trans_load_header modify(seal_no varchar2(1000));


--caijiante  2017-05-12
--SYS_FUNC_PAGE加一条记录
963?	22964	P02_B09_P0_08	P02_B09_P0	导出	150	Y	2015/5/27	wpsadmin	导出	T	

--yuanlei 2017-05-15
create or replace procedure SP_IMPORT_ORDER(
IN_TMPID varchar2,
IN_USERID varchar2,
IN_LANGUAGE VARCHAR2,
OUTPUT_RESULLT in out varchar2)
IS

t_cur_column varchar2(100);
t_cur_value varchar2(500);
t_verify_type number(4);
t_table_name varchar2(100);
t_verify_code varchar2(100);

v_sql varchar2(2048);
temp_sql varchar2(1024);
temp_result varchar2(500);
tmp_verify_code varchar2(100);

r_REFENENCE1 varchar2(100);
r_CUSTOM_ODR_NO varchar2(100);
r_CUSTOMER_NAME varchar2(100);
r_BIZ_TYP varchar2(100);
r_ODR_TYP varchar2(100);
r_ODR_TIME varchar2(100);
r_VEHICLE_TYP varchar2(100);
r_TEMPERATURE1 varchar2(100);
r_TEMPERATURE2 varchar2(100);
r_PRE_LOAD_TIME varchar2(100);
r_PRE_UNLOAD_TIME varchar2(100);
r_SLF_PICKUP_FLAG varchar2(10);
r_SLF_DELIVER_FLAG varchar2(10);
r_NOTES varchar2(500);
r_LOAD_CODE varchar2(100);
r_LOAD_AREA_NAME varchar2(100);
r_LOAD_AREA_NAME2 varchar2(100);
r_LOAD_AREA_NAME3 varchar2(100);
r_LOAD_ADDRESS varchar2(300);
r_LOAD_CONTACT varchar2(100);
r_LOAD_TEL varchar2(100);
r_UNLOAD_CODE varchar2(100);
r_UNLOAD_AREA_NAME varchar2(100);
r_UNLOAD_AREA_NAME2 varchar2(100);
r_UNLOAD_AREA_NAME3 varchar2(100);
r_UNLOAD_ADDRESS varchar2(300);
r_UNLOAD_CONTACT varchar2(100);
r_UNLOAD_TEL varchar2(100);
r_SKU varchar2(100);
r_SKU_NAME varchar2(200);
r_UOM varchar2(100);
r_QNTY varchar2(100);
r_TEMPERATURE varchar2(100);
r_G_WGT varchar2(100);
r_VOL varchar2(100);
r_LINE number(4);
r_USERID varchar2(30);
r_BTCH_NUM VARCHAR2(50);

type   cur_t   is   ref   cursor;
      C_ACT   cur_t;

TYPE array_var IS VARRAY(37) OF nvarchar2(10000);

tmp_key array_var;
tmp_value array_var;

tmp_table varchar2(255);
tmp_column varchar2(255);
tmp_param_type NUMBER;
tmp_replace_field varchar2(100);
tmp_default_value varchar2(255);

exec_sql varchar2(4096);
exec_value varchar2(4096);

r_count number(4);
pos number(4);

load_code varchar2(100);
unload_code varchar2(100);

op_customer_id varchar2(32);
op_customer_name VARCHAR2(50);
op_load_area_name varchar2(50);
op_load_area_name2 varchar2(50);
op_load_area_name3 varchar2(50);
op_unload_area_name varchar2(50);
op_unload_area_name2 varchar2(50);
op_unload_area_name3 varchar2(50);
op_load_address varchar2(300);
op_load_contact varchar2(40);
op_load_tel varchar2(80);
op_unload_address varchar2(300);
op_unload_contact varchar2(40);
op_unload_tel varchar2(80);
op_load_area_id varchar2(32);
op_load_area_id2 varchar2(32);
op_load_area_id3 varchar2(32);
op_load_name varchar2(100);
op_unload_area_id varchar2(32);
op_unload_area_id2 varchar2(32);
op_unload_area_id3 varchar2(32);
op_unload_name varchar2(100);
op_exec_org_id varchar2(32);
op_id varchar2(32);
op_odr_no varchar2(100);
op_odr_row number(4);
op_skuname varchar2(100);
op_packid varchar2(32);

odr_lst LST := LST();

tmp_unload_address varchar2(300);
tmp_unload_contact varchar2(40);
tmp_unload_tel varchar2(40);
tmp_load_address  varchar2(300);
tmp_load_contact varchar2(40);
tmp_load_tel varchar2(80);


CURSOR verify is
      select verify_type,table_name,verify_code from t_input_excel_verify where type_name = 'TMP_ORDER_IMPORT' and COLUMN_NAME = t_cur_column;
CURSOR head is
      select column_name,default_value,param_type,replace_field,table_name from t_input_excel_default where table_name = 'TRANS_ORDER_HEADER' order by exec_seq asc;
CURSOR item is
      select column_name,default_value,param_type,replace_field,table_name from t_input_excel_default where table_name = 'TRANS_ORDER_ITEM' order by exec_seq asc;
--IN_TMPID varchar2(32);
BEGIN
  --IN_TMPID := '1489116521217';
  OUTPUT_RESULLT := '';
  --return;
  tmp_key := array_var('REFENENCE1','CUSTOM_ODR_NO','CUSTOMER_NAME','BIZ_TYP','ODR_TYP','ODR_TIME','VEHICLE_TYP','TEMPERATURE1','TEMPERATURE2','SLF_PICKUP_FLAG'
  ,'SLF_DELIVER_FLAG','NOTES','REQ_LOAD_TIME','REQ_UNLOAD_TIME','LOAD_CODE','LOAD_AREA_NAME','LOAD_AREA_NAME2','LOAD_AREA_NAME3','LOAD_ADDRESS','LOAD_CONTACT'
  ,'LOAD_TEL','UNLOAD_CODE','UNLOAD_AREA_NAME','UNLOAD_AREA_NAME2','UNLOAD_AREA_NAME3','UNLOAD_ADDRESS','UNLOAD_CONTACT','UNLOAD_TEL','SKU','SKU_NAME','UOM'
  ,'QNTY','TEMPERATURE','G_WGT','VOL','BTCH_NUM','USER_ID');
  v_sql := '';
  r_LINE := 1;
  pos := 1;
  for i in 1..tmp_key.count loop
      t_cur_column := tmp_key(i);
      v_sql := v_sql|| t_cur_column || ',';
  end loop;
  v_sql := 'SELECT ' || v_sql || 'LINENO FROM TMP_ORDER_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' ORDER BY LINENO ASC';
  --校验字段
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;

      tmp_value := array_var(r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID);

      if r_CUSTOMER_NAME is null then  --当客户单号为空时，可认为与上一行信息相同，直接跳到明细列进行校验。
          pos := 15;
      end if;

      for i in pos..tmp_key.count loop
           t_cur_column := tmp_key(i);
           t_cur_value := tmp_value(i);
           for m in verify loop
               t_verify_type := m.verify_type;
               t_table_name := m.table_name;
               t_verify_code := m.verify_code;

               if t_verify_type = 0 then  --非空检验
                   if t_cur_value is null or length(t_cur_value) < 1 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行字段['|| t_cur_column ||']不能为空<br />';
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   end if;
               elsif t_verify_type = 1 and length(t_cur_value) > 0 then --唯一性校验
                   temp_sql := 'select count(1) from ' || t_table_name || ' where ' || t_cur_column || '=''' || t_cur_value || '''';
                   execute immediate temp_sql into r_count;
                   if r_count > 0 then
                        OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行['|| t_cur_column ||']列不是唯一<br />';
                        if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                        end if;
                   else
                      temp_sql := 'select count(1) from TMP_ORDER_IMPORT where TMPID = ''' || IN_TMPID || ''' AND USER_ID = '''
                        || IN_USERID || ''' and ' || t_cur_column || '= ''' || t_cur_value || '''';
                      execute immediate temp_sql into r_count;
                      if r_count > 1 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行['|| t_cur_column ||']列不是唯一<br />';
                          if length(OUTPUT_RESULLT) > 1000 then
                               OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                               return;
                           end if;
                      end if;
                   end if;
               elsif t_verify_type = 2 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行['|| t_cur_column ||']列值为[' || t_cur_value || '],数值不合法<br />';
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   else
                      if t_cur_column = 'SKU' then  --如果是只填写SKU，则将SKU对应的名称写到临时表，避免校验SKU_NAME时因为无值不通过
                          select sku_cname into op_skuname from BAS_SKU where sku = t_cur_value;
                          update TMP_ORDER_IMPORT set SKU_NAME = op_skuname
                            WHERE TMPID = IN_TMPID AND USER_ID = IN_USERID AND LINENO = r_LINE;
                          tmp_value(30) := op_skuname;
                      end if;
                   end if;
               elsif t_verify_type = 3 then   --二选一非空校验
                   if t_cur_value is null then
                       select verify_code into tmp_verify_code from t_input_excel_verify where column_name = t_verify_code and type_name = 'TMP_ORDER_IMPORT';
                       temp_sql := 'select ' || t_verify_code || ' from TMP_ORDER_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' and LINENO = ''' || r_LINE || '''';
                       execute immediate temp_sql into temp_result;
                       temp_sql := REPLACE(tmp_verify_code, '?', temp_result);
                       execute immediate temp_sql into r_count;
                       if r_count = 0 then
                           OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行'|| t_cur_column ||']值不合法<br />';
                           if length(OUTPUT_RESULLT) > 1000 then
                               OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                               return;
                           end if;
                       end if;
                   end if;
               elsif t_verify_type = 4 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then --长度校验
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行['|| t_cur_column ||']列内容超长!<br />';
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   end if;
               end if;
           end loop;
      end loop;
      <<next_loop>>
          t_cur_column := '';
  end loop;
  close C_ACT;

  if length(OUTPUT_RESULLT) > 0 then
    OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
    rollback;
    -- 失败删除中间表
    delete from TMP_ORDER_IMPORT where TMPID = IN_TMPID;
    commit;
    return;
  end if;
  --插入数据
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;

      tmp_value := array_var(r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID);

      exec_sql := '';
      exec_value := '';
      if r_CUSTOMER_NAME is not null then
          for h in head loop
              tmp_table := h.table_name;
              tmp_column := h.column_name;
              tmp_param_type := h.param_type;
              tmp_replace_field := h.replace_field;
              tmp_default_value := h.default_value;
              exec_sql := exec_sql || ',' || h.column_name;
              if h.param_type = '0' then  --定制
                  exec_value := exec_value || ',' || h.default_value;
              elsif h.param_type = '1' then  --从执行结果取值
                  temp_sql := h.default_value;
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
                  for i in 1..tmp_key.count loop
                      t_cur_column := tmp_key(i);
                      if t_cur_column = h.Replace_Field then
                          t_cur_value := tmp_value(i);
                          exit;
                      end if;
                  end loop;
                  if t_cur_value is not null then
                      temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                      execute immediate temp_sql into temp_result;
                      exec_value := exec_value || ',''' || temp_result || '''';
                  else
                      exec_value := exec_value || ',NULL';
                  end if;
                  if  h.column_name = 'CUSTOMER_ID' then
                      op_customer_id := temp_result;
                  elsif h.column_name = 'CUSTOMER_NAME' then
                      op_customer_name := temp_result;
                  end if;
              elsif h.param_type = '3' then  --直接从临时表上取值
                  for i in 1..tmp_key.count loop
                      t_cur_column := tmp_key(i);
                      if t_cur_column = h.column_name then
                          t_cur_value := tmp_value(i);
                          exit;
                      end if;
                  end loop;
                  if t_cur_value is null and h.column_name = 'CUSTOMER_NAME' then
                     goto next_loop;
                  end if;
                  if t_cur_value is null then
                      exec_value := exec_value || ',NULL';
                  else
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
             end if;
          end loop;
      end if;
      <<next_loop>>
          OUTPUT_RESULLT := '';
      if length(exec_value) > 1 then
          --获取托运单号
          SP_GET_IDSEQ('ORDER',op_odr_no);

          exec_sql := 'INSERT INTO TRANS_ORDER_HEADER(ODR_NO,ADDWHO,ADDTIME' || exec_sql || ') VALUES(''' || op_odr_no || ''',''' || IN_USERID || ''',sysdate' || exec_value || ')';
          execute immediate exec_sql;

          odr_lst.extend();
          odr_lst(odr_lst.count) := op_odr_no;
      end if;
      exec_sql := '';
      exec_value := '';
      op_packid := '2EEB495B515049B3A9CD76572DDDC999';
      for h in item loop
          tmp_table := h.table_name;
          tmp_column := h.column_name;
          tmp_param_type := h.param_type;
          tmp_replace_field := h.replace_field;
          tmp_default_value := h.default_value;
          if h.param_type = '0' then  --定制
              exec_sql := exec_sql || ',' || h.column_name;
              exec_value := exec_value || ',' || h.default_value;
          elsif h.param_type = '1' then  --从执行结果取值
              exec_sql := exec_sql || ',' || h.column_name;
              temp_sql := h.default_value;
              execute immediate temp_sql into temp_result;
              exec_value := exec_value || ',''' || temp_result || '''';
          elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
              exec_sql := exec_sql || ',' || h.column_name;
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.replace_field then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is not null then
                  temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              else
                  exec_value := exec_value || ',NULL';
              end if;
          elsif h.param_type = '3' then  --直接从临时表上取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.column_name then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if h.column_name = 'LOAD_CODE' then
                  load_code := t_cur_value;
                  if load_code is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_CODE' then
                  unload_code := t_cur_value;
                  if unload_code is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME' then
                  op_unload_area_name := t_cur_value;
                  if op_unload_area_name is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME2' then
                  op_unload_area_name2 := t_cur_value;
                  if op_unload_area_name2 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value  || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME3' then
                  op_unload_area_name3 := t_cur_value;
                  if op_unload_area_name3 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_ADDRESS' then
                  op_unload_address := t_cur_value;
                  if op_unload_address is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_CONTACT' then
                  op_unload_contact := t_cur_value;
                  if op_unload_contact is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_TEL' then
                  op_unload_tel := t_cur_value;
                  if op_unload_tel is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME' then
                  op_load_area_name := t_cur_value;
                  if op_load_area_name is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME2' then
                  op_load_area_name2 := t_cur_value;
                  if op_load_area_name2 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME3' then
                  op_load_area_name3 := t_cur_value;
                  if op_load_area_name3 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_ADDRESS' then
                  op_load_address := t_cur_value;
                  if op_load_address is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_CONTACT' then
                  op_load_contact := t_cur_value;
                  if op_load_contact is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_TEL' then
                  op_load_tel := t_cur_value;
                  if op_load_tel is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'SKU' then
                  if t_cur_value is not null then
                      select id into op_packid from bas_sku where sku = t_cur_value;
                  end if;
                  exec_sql := exec_sql || ',' || h.column_name;
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              else
                  exec_sql := exec_sql || ',' || h.column_name;
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              end if;
         elsif h.param_type = '4' then --发货地址特殊处理
              if load_code is null then  --先自动录入地址信息再处理
                  select F_GET_DEFAULT_ORGBYUSER(IN_USERID, 0) into op_exec_org_id from dual;
                  select sys_guid() into op_id from dual;
                  select area_code into op_load_area_id from bas_area where area_cname=op_load_area_name and area_level=3 and rownum=1;
                  select area_code into op_load_area_id2 from bas_area where area_cname=op_load_area_name2 and area_level=4 and rownum=1;
                  load_code := substr(F_ZJM(op_load_address),0,32);
                  op_load_name := substr(op_load_address,0,100);
                  if op_load_area_name3 is not null then
                      select area_code into op_load_area_id3 from bas_area where area_cname=op_load_area_name3 and area_level=5 and rownum=1;
                  end if;

                  select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = load_code;
                  if r_count > 0 then
                      select id,addr_name,area_id,area_id2,area_id3 into op_id,op_load_name,op_load_area_id,op_load_area_id2,op_load_area_id3
                        from bas_address where customer_id = op_customer_id and addr_code = load_code;
                  else
                      --插入地址
                      insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
                        cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
                      values(op_id,load_code,op_customer_id,op_customer_name,'Y','N','N',op_load_area_id,op_load_area_name,op_load_name,op_load_address,
                        op_load_contact,op_load_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,IN_USERID,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3);
                  end if;
                  --exec_value := exec_value || ',''' || op_id || '''';

                  exec_sql := exec_sql || ',LOAD_ID,LOAD_CODE,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_ID2,LOAD_AREA_ID3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || load_code || ''',''' || op_load_name || ''',''' || op_load_area_id || ''','''
                     || op_load_area_id2 || ''',''' || op_load_area_id3 || '''';
              else
                  select count(1) into r_count from bas_address where addr_code = load_code;
                  if r_count > 1 then
                      select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = load_code;
                      if r_count > 1 or r_count = 0 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行收货方代码['|| unload_code ||']重复<br />';
                          for h in 1..odr_lst.count loop
                              op_odr_no := odr_lst(h);
                              delete from trans_order_header where odr_no = op_odr_no;
                              delete from trans_order_item where odr_no = op_odr_no;
                          end loop;
                          commit;
                          return;
                      else
                          select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address
                          into op_id,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,tmp_load_contact,tmp_load_tel,tmp_load_address
                          from bas_address where customer_id = op_customer_id and addr_code = load_code; 
                      end if;
                  else
                      select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address
                      into op_id,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,tmp_load_contact,tmp_load_tel,tmp_load_address
                      from bas_address where addr_code = load_code;
                  end if;

                  exec_sql := exec_sql || ',LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || op_load_name || ''',''' || op_load_area_id || ''',''' || op_load_area_name ||''','''
                     || op_load_area_id2 || ''',''' || op_load_area_name2 ||''',''' || op_load_area_id3 || ''',''' || op_load_area_name3 || '''';
                  if op_load_address is null then
                      exec_sql := exec_sql || ',LOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_load_address || '''';
                  end if;
                  if op_load_contact is null then
                      exec_sql := exec_sql || ',LOAD_CONTACT';
                      exec_value := exec_value || ',''' || tmp_load_contact || '''';
                  end if;
                  if op_load_tel is null then
                      exec_sql := exec_sql || ',LOAD_TEL';
                      exec_value := exec_value || ',''' || tmp_load_tel || '''';
                  end if;
              end if;
              --exec_value := exec_value || ',' || t_cur_value;
         elsif h.param_type = '5' then --收货地址特殊处理
              if unload_code is null then  --先自动录入地址信息再处理
                  select F_GET_DEFAULT_ORGBYUSER(IN_USERID, 0) into op_exec_org_id from dual;
                  select sys_guid() into op_id from dual;
                  select area_code into op_unload_area_id from bas_area where area_cname=op_unload_area_name and area_level=3 and rownum=1;
                  select area_code into op_unload_area_id2 from bas_area where area_cname=op_unload_area_name2 and area_level=4 and rownum=1;
                  unload_code := substr(F_ZJM(op_unload_address),0,32);
                  op_unload_name := substr(op_unload_address,0,100);
                  if op_unload_area_name3 is not null then
                      select area_code into op_unload_area_id3 from bas_area where area_cname=op_unload_area_name3 and area_level=5 and rownum=1;
                  end if;

                  select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                  if r_count > 0 then
                      select id,addr_name,area_id,area_id2,area_id3 into op_id,op_unload_name,op_unload_area_id,op_unload_area_id2,op_unload_area_id3
                      from bas_address where addr_code = unload_code and customer_id = op_customer_id;
                  else
                      --插入地址
                      insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
                        cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
                      values(op_id,unload_code,op_customer_id,op_customer_name,'N','N','Y',op_unload_area_id,op_unload_area_name,op_unload_name,op_unload_address,
                        op_unload_contact,op_unload_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,IN_USERID,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3);
                  end if;
                  --exec_value := exec_value || ',''' || op_id || '''';

                  exec_sql := exec_sql || ',UNLOAD_ID,UNLOAD_CODE,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_ID2,UNLOAD_AREA_ID3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || unload_code || ''',''' || op_unload_name || ''',''' || op_unload_area_id || ''','''
                     || op_unload_area_id2 || ''',''' || op_unload_area_id3 || '''';
              else
                  select count(1) into r_count from bas_address where addr_code = unload_code;
                  if r_count > 1 then
                      select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                      if r_count > 1 or r_count = 0 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '第'|| r_LINE ||'行收货方代码['|| unload_code ||']重复<br />';
                          for h in 1..odr_lst.count loop
                              op_odr_no := odr_lst(h);
                              delete from trans_order_header where odr_no = op_odr_no;
                              delete from trans_order_item where odr_no = op_odr_no;
                          end loop;
                          commit;
                          return;
                      else
                          select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,address,cont_name,cont_tel
                          into op_id,op_unload_name,op_unload_area_id,op_unload_area_name,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3,tmp_unload_address,tmp_unload_contact,tmp_unload_tel
                          from bas_address where customer_id = op_customer_id and addr_code = unload_code; 
                      end if;
                  else
                      select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,address,cont_name,cont_tel
                      into op_id,op_unload_name,op_unload_area_id,op_unload_area_name,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3,tmp_unload_address,tmp_unload_contact,tmp_unload_tel
                      from bas_address where addr_code = unload_code;
                  end if;
                  exec_sql := exec_sql || ',UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || op_unload_name || ''',''' || op_unload_area_id || ''',''' || op_unload_area_name || ''','''
                     || op_unload_area_id2 || ''',''' || op_unload_area_name2 || ''',''' || op_unload_area_id3 || ''',''' || op_unload_area_name3 || '''';
                  if op_unload_address is null then
                      exec_sql := exec_sql || ',UNLOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_unload_address || '''';
                  end if;
                  if op_unload_contact is null then
                      exec_sql := exec_sql || ',UNLOAD_CONTACT';
                      exec_value := exec_value || ',''' || tmp_unload_contact || '''';
                  end if;
                  if op_unload_tel is null then
                      exec_sql := exec_sql || ',UNLOAD_TEL';
                      exec_value := exec_value || ',''' || tmp_unload_tel || '''';
                  end if;
              end if;
              --exec_value := exec_value || ',' || t_cur_value;
         end if;
         <<next_loop>>
             OUTPUT_RESULLT := '';
      end loop;
      select nvl(max(odr_row),0) + 1 into op_odr_row from trans_order_item where odr_no = op_odr_no;
      exec_sql := 'INSERT INTO TRANS_ORDER_ITEM(ODR_NO,ODR_ROW,ADDWHO,PACK_ID,ADDTIME' || exec_sql || ') VALUES(''' || op_odr_no || ''',' || op_odr_row || ',''' || IN_USERID || ''',''' || op_packid ||''',sysdate' || exec_value || ')';
      execute immediate exec_sql;
      
      ORDER_QTY_COUNT(op_odr_no, OUTPUT_RESULLT); --汇总订单信息
      --更新订单发货点和收货点信息
      UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select min(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select max(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      
      OUTPUT_RESULLT := '00';
  end loop;
  close C_ACT;
  commit; 

  delete from TMP_ORDER_IMPORT WHERE TMPID = IN_TMPID;
  /*for h in 1..odr_lst.count loop
      op_odr_no := odr_lst(h);
      ORDER_QTY_COUNT(op_odr_no, OUTPUT_RESULLT); --汇总订单信息
      --更新订单发货点和收货点信息
      UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select min(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select max(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
  end loop;*/
  OUTPUT_RESULLT := '00';
  commit;
  EXCEPTION
    WHEN OTHERS THEN
        --OUTPUT_RESULLT :='01'||sqlerrm || ']';
        OUTPUT_RESULLT := '01' || OUTPUT_RESULLT || '第'|| r_LINE ||'行['|| t_cur_column ||']列异常,请联系管理员!<br />';
        rollback;
        
        insert into t_input_excel_temp(type_name,VALUE,line,column_name,column_cname,id,custom_odr_no) 
        values(tmp_param_type,tmp_default_value,r_LINE,tmp_column,substr(OUTPUT_RESULLT,0,199),to_char(sysdate,'YYYY-MM-DD HH24:MI'),tmp_table);
        
        delete from TMP_ORDER_IMPORT WHERE TMPID = IN_TMPID;
        for h in 1..odr_lst.count loop
            op_odr_no := odr_lst(h);
            delete from trans_order_header where odr_no = op_odr_no;
            delete from trans_order_item where odr_no = op_odr_no;
        end loop;
        commit;
        --RAISE_APPLICATION_ERROR(-20101, '执行全局校验时抛出异常: '||sqlerrm, TRUE);
END;
/

CREATE OR REPLACE FUNCTION IS_Number (
   str_    VARCHAR2 ) RETURN VARCHAR2
IS
   num_    NUMBER;
BEGIN
  num_ := to_number(str_);
  return '1';
EXCEPTION
   WHEN OTHERS THEN
      RETURN '0';
END Is_Number;

/

create or replace procedure SP_IMPORT_ORDER(
IN_TMPID varchar2,
IN_USERID varchar2,
IN_LANGUAGE VARCHAR2,
OUTPUT_RESULLT in out varchar2)
IS

t_cur_column varchar2(100);
t_cur_value varchar2(500);
t_cur_name varchar2(100);
t_msg varchar2(200);
t_verify_type number(4);
t_table_name varchar2(100);
t_verify_code varchar2(100);

v_sql varchar2(2048);
temp_sql varchar2(1024);
temp_result varchar2(500);
tmp_verify_code varchar2(100);

r_REFENENCE1 varchar2(100);
r_CUSTOM_ODR_NO varchar2(100);
r_CUSTOMER_NAME varchar2(100);
r_BIZ_TYP varchar2(100);
r_ODR_TYP varchar2(100);
r_ODR_TIME varchar2(100);
r_VEHICLE_TYP varchar2(100);
r_TEMPERATURE1 varchar2(100);
r_TEMPERATURE2 varchar2(100);
r_PRE_LOAD_TIME varchar2(100);
r_PRE_UNLOAD_TIME varchar2(100);
r_SLF_PICKUP_FLAG varchar2(10);
r_SLF_DELIVER_FLAG varchar2(10);
r_NOTES varchar2(500);
r_LOAD_CODE varchar2(100);
r_LOAD_AREA_NAME varchar2(100);
r_LOAD_AREA_NAME2 varchar2(100);
r_LOAD_AREA_NAME3 varchar2(100);
r_LOAD_ADDRESS varchar2(300);
r_LOAD_CONTACT varchar2(100);
r_LOAD_TEL varchar2(100);
r_UNLOAD_CODE varchar2(100);
r_UNLOAD_AREA_NAME varchar2(100);
r_UNLOAD_AREA_NAME2 varchar2(100);
r_UNLOAD_AREA_NAME3 varchar2(100);
r_UNLOAD_ADDRESS varchar2(300);
r_UNLOAD_CONTACT varchar2(100);
r_UNLOAD_TEL varchar2(100);
r_SKU varchar2(100);
r_SKU_NAME varchar2(200);
r_UOM varchar2(100);
r_QNTY varchar2(100);
r_TEMPERATURE varchar2(100);
r_G_WGT varchar2(100);
r_VOL varchar2(100);
r_LINE number(4);
r_USERID varchar2(30);
r_BTCH_NUM VARCHAR2(50);

type   cur_t   is   ref   cursor;
      C_ACT   cur_t;

TYPE array_var IS VARRAY(37) OF nvarchar2(10000);

tmp_key array_var;
tmp_name array_var;
tmp_value array_var;

tmp_table varchar2(255);
tmp_column varchar2(255);
tmp_param_type NUMBER;
tmp_replace_field varchar2(100);
tmp_default_value varchar2(255);

exec_sql varchar2(4096);
exec_value varchar2(4096);

r_count number(4);
pos number(4);

load_code varchar2(100);
unload_code varchar2(100);

op_customer_id varchar2(32);
op_customer_name VARCHAR2(50);
op_load_area_name varchar2(50);
op_load_area_name2 varchar2(50);
op_load_area_name3 varchar2(50);
op_unload_area_name varchar2(50);
op_unload_area_name2 varchar2(50);
op_unload_area_name3 varchar2(50);
op_load_address varchar2(300);
op_load_contact varchar2(40);
op_load_tel varchar2(80);
op_unload_address varchar2(300);
op_unload_contact varchar2(40);
op_unload_tel varchar2(80);
op_load_area_id varchar2(32);
op_load_area_id2 varchar2(32);
op_load_area_id3 varchar2(32);
op_load_name varchar2(100);
op_unload_area_id varchar2(32);
op_unload_area_id2 varchar2(32);
op_unload_area_id3 varchar2(32);
op_unload_name varchar2(100);
op_exec_org_id varchar2(32);
op_id varchar2(32);
op_odr_no varchar2(100);
op_odr_row number(4);
op_skuname varchar2(100);
op_packid varchar2(32);

odr_lst LST := LST();

tmp_unload_address varchar2(300);
tmp_unload_contact varchar2(40);
tmp_unload_tel varchar2(40);
tmp_load_address  varchar2(300);
tmp_load_contact varchar2(40);
tmp_load_tel varchar2(80);


CURSOR verify is
      select verify_type,table_name,verify_code,error_msg from t_input_excel_verify where type_name = 'TMP_ORDER_IMPORT' and COLUMN_NAME = t_cur_column;
CURSOR head is
      select column_name,default_value,param_type,replace_field,table_name from t_input_excel_default where table_name = 'TRANS_ORDER_HEADER' order by exec_seq asc;
CURSOR item is
      select column_name,default_value,param_type,replace_field,table_name from t_input_excel_default where table_name = 'TRANS_ORDER_ITEM' order by exec_seq asc;
--IN_TMPID varchar2(32);
BEGIN
  --IN_TMPID := '1489116521217';
  OUTPUT_RESULLT := '';
  --return;
  tmp_key := array_var('REFENENCE1','CUSTOM_ODR_NO','CUSTOMER_NAME','BIZ_TYP','ODR_TYP','ODR_TIME','VEHICLE_TYP','TEMPERATURE1','TEMPERATURE2','SLF_PICKUP_FLAG'
  ,'SLF_DELIVER_FLAG','NOTES','PRE_LOAD_TIME','PRE_UNLOAD_TIME','LOAD_CODE','LOAD_AREA_NAME','LOAD_AREA_NAME2','LOAD_AREA_NAME3','LOAD_ADDRESS','LOAD_CONTACT'
  ,'LOAD_TEL','UNLOAD_CODE','UNLOAD_AREA_NAME','UNLOAD_AREA_NAME2','UNLOAD_AREA_NAME3','UNLOAD_ADDRESS','UNLOAD_CONTACT','UNLOAD_TEL','SKU','SKU_NAME','UOM'
  ,'QNTY','TEMPERATURE','G_WGT','VOL','BTCH_NUM','USER_ID');
  
  tmp_name := array_var('运单号','客户订单号','客户代码','业务类型','运输类型','下单时间','车辆类型','温度下限','温度上限','客户自提'
  ,'客户自送','备注','要求发货时间','要求到货时间','发货点代码','发货省','发货市','发货区','发货详细地址','发货联系人'
  ,'发货联系电话','卸货点代码','卸货省','卸货市','卸货区','卸货详细地址','卸货联系人','卸货联系电话','货品代码','货品名称','运输单位'
  ,'数量','温区','毛重(千克)','体积（立方）','订单组号','登录用户');
  v_sql := '';
  r_LINE := 1;
  pos := 1;
  for i in 1..tmp_key.count loop
      t_cur_column := tmp_key(i);
      v_sql := v_sql|| t_cur_column || ',';
  end loop;
  v_sql := 'SELECT ' || v_sql || 'LINENO FROM TMP_ORDER_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' ORDER BY LINENO ASC';
  --校验字段
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;

      tmp_value := array_var(r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID);

      if r_CUSTOMER_NAME is null then  --当客户单号为空时，可认为与上一行信息相同，直接跳到明细列进行校验。
          pos := 15;
      end if;

      for i in pos..tmp_key.count loop
           t_cur_column := tmp_key(i);
           t_cur_value := tmp_value(i);
           t_cur_name := tmp_name(i);
           for m in verify loop
               t_verify_type := m.verify_type;
               t_table_name := m.table_name;
               t_verify_code := m.verify_code;
               t_msg := m.error_msg;

               if t_verify_type = 0 then  --非空检验
                   if t_cur_value is null or length(t_cur_value) < 1 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   end if;
               elsif t_verify_type = 1 and length(t_cur_value) > 0 then --唯一性校验
                   temp_sql := 'select count(1) from ' || t_table_name || ' where ' || t_cur_column || '=''' || t_cur_value || '''';
                   execute immediate temp_sql into r_count;
                   if r_count > 0 then
                        OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                        if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                        end if;
                   else
                      temp_sql := 'select count(1) from TMP_ORDER_IMPORT where TMPID = ''' || IN_TMPID || ''' AND USER_ID = '''
                        || IN_USERID || ''' and ' || t_cur_column || '= ''' || t_cur_value || '''';
                      execute immediate temp_sql into r_count;
                      if r_count > 1 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                          if length(OUTPUT_RESULLT) > 1000 then
                               OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                               return;
                           end if;
                      end if;
                   end if;
               elsif t_verify_type = 2 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列值为[' || t_cur_value || '],' || t_msg;
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   else
                      if t_cur_column = 'SKU' then  --如果是只填写SKU，则将SKU对应的名称写到临时表，避免校验SKU_NAME时因为无值不通过
                          select sku_cname into op_skuname from BAS_SKU where sku = t_cur_value;
                          update TMP_ORDER_IMPORT set SKU_NAME = op_skuname
                            WHERE TMPID = IN_TMPID AND USER_ID = IN_USERID AND LINENO = r_LINE;
                          tmp_value(30) := op_skuname;
                      end if;
                   end if;
               elsif t_verify_type = 3 then   --二选一非空校验
                   if t_cur_value is null then
                       select verify_code into tmp_verify_code from t_input_excel_verify where column_name = t_verify_code and verify_type = '2' and type_name = 'TMP_ORDER_IMPORT';
                       temp_sql := 'select ' || t_verify_code || ' from TMP_ORDER_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' and LINENO = ''' || r_LINE || '''';
                       execute immediate temp_sql into temp_result;
                       temp_sql := REPLACE(tmp_verify_code, '?', temp_result);
                       execute immediate temp_sql into r_count;
                       if r_count = 0 then
                           OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                           if length(OUTPUT_RESULLT) > 1000 then
                               OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                               return;
                           end if;
                       end if;
                   end if;
               elsif t_verify_type = 4 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then --长度校验
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   end if;
               end if;
           end loop;
      end loop;
      <<next_loop>>
          t_cur_column := '';
  end loop;
  close C_ACT;

  if length(OUTPUT_RESULLT) > 0 then
    OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
    rollback;
    -- 失败删除中间表
    delete from TMP_ORDER_IMPORT where TMPID = IN_TMPID;
    commit;
    return;
  end if;
  --插入数据
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;

      tmp_value := array_var(r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID);

      exec_sql := '';
      exec_value := '';
      if r_CUSTOMER_NAME is not null then
          for h in head loop
              tmp_table := h.table_name;
              tmp_column := h.column_name;
              tmp_param_type := h.param_type;
              tmp_replace_field := h.replace_field;
              tmp_default_value := h.default_value;
              exec_sql := exec_sql || ',' || h.column_name;
              if h.param_type = '0' then  --定制
                  exec_value := exec_value || ',' || h.default_value;
              elsif h.param_type = '1' then  --从执行结果取值
                  temp_sql := h.default_value;
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
                  for i in 1..tmp_key.count loop
                      t_cur_column := tmp_key(i);
                      if t_cur_column = h.Replace_Field then
                          t_cur_value := tmp_value(i);
                          exit;
                      end if;
                  end loop;
                  if t_cur_value is not null then
                      temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                      execute immediate temp_sql into temp_result;
                      exec_value := exec_value || ',''' || temp_result || '''';
                  else
                      exec_value := exec_value || ',NULL';
                  end if;
                  if  h.column_name = 'CUSTOMER_ID' then
                      op_customer_id := temp_result;
                  elsif h.column_name = 'CUSTOMER_NAME' then
                      op_customer_name := temp_result;
                  end if;
              elsif h.param_type = '3' then  --直接从临时表上取值
                  for i in 1..tmp_key.count loop
                      t_cur_column := tmp_key(i);
                      if t_cur_column = h.column_name then
                          t_cur_value := tmp_value(i);
                          exit;
                      end if;
                  end loop;
                  if t_cur_value is null and h.column_name = 'CUSTOMER_NAME' then
                     goto next_loop;
                  end if;
                  if t_cur_value is null then
                      exec_value := exec_value || ',NULL';
                  else
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
             end if;
          end loop;
      end if;
      <<next_loop>>
          OUTPUT_RESULLT := '';
      if length(exec_value) > 1 then
          --获取托运单号
          SP_GET_IDSEQ('ORDER',op_odr_no);

          exec_sql := 'INSERT INTO TRANS_ORDER_HEADER(ODR_NO,ADDWHO,ADDTIME' || exec_sql || ') VALUES(''' || op_odr_no || ''',''' || IN_USERID || ''',sysdate' || exec_value || ')';
          execute immediate exec_sql;

          odr_lst.extend();
          odr_lst(odr_lst.count) := op_odr_no;
      end if;
      exec_sql := '';
      exec_value := '';
      op_packid := '2EEB495B515049B3A9CD76572DDDC999';
      for h in item loop
          tmp_table := h.table_name;
          tmp_column := h.column_name;
          tmp_param_type := h.param_type;
          tmp_replace_field := h.replace_field;
          tmp_default_value := h.default_value;
          if h.param_type = '0' then  --定制
              exec_sql := exec_sql || ',' || h.column_name;
              exec_value := exec_value || ',' || h.default_value;
          elsif h.param_type = '1' then  --从执行结果取值
              exec_sql := exec_sql || ',' || h.column_name;
              temp_sql := h.default_value;
              execute immediate temp_sql into temp_result;
              exec_value := exec_value || ',''' || temp_result || '''';
          elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
              exec_sql := exec_sql || ',' || h.column_name;
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.replace_field then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is not null then
                  temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
              else
                  exec_value := exec_value || ',NULL';
              end if;
          elsif h.param_type = '3' then  --直接从临时表上取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.column_name then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if h.column_name = 'LOAD_CODE' then
                  load_code := t_cur_value;
                  if load_code is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_CODE' then
                  unload_code := t_cur_value;
                  if unload_code is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME' then
                  op_unload_area_name := t_cur_value;
                  if op_unload_area_name is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME2' then
                  op_unload_area_name2 := t_cur_value;
                  if op_unload_area_name2 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value  || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME3' then
                  op_unload_area_name3 := t_cur_value;
                  if op_unload_area_name3 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_ADDRESS' then
                  op_unload_address := t_cur_value;
                  if op_unload_address is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_CONTACT' then
                  op_unload_contact := t_cur_value;
                  if op_unload_contact is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'UNLOAD_TEL' then
                  op_unload_tel := t_cur_value;
                  if op_unload_tel is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME' then
                  op_load_area_name := t_cur_value;
                  if op_load_area_name is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME2' then
                  op_load_area_name2 := t_cur_value;
                  if op_load_area_name2 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME3' then
                  op_load_area_name3 := t_cur_value;
                  if op_load_area_name3 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_ADDRESS' then
                  op_load_address := t_cur_value;
                  if op_load_address is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_CONTACT' then
                  op_load_contact := t_cur_value;
                  if op_load_contact is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'LOAD_TEL' then
                  op_load_tel := t_cur_value;
                  if op_load_tel is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                  end if;
              elsif h.column_name = 'SKU' then
                  if t_cur_value is not null then
                      select id into op_packid from bas_sku where sku = t_cur_value;
                  end if;
                  exec_sql := exec_sql || ',' || h.column_name;
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              else
                  exec_sql := exec_sql || ',' || h.column_name;
                  exec_value := exec_value || ',''' || t_cur_value || '''';
              end if;
         elsif h.param_type = '4' then --发货地址特殊处理
              if load_code is null then  --先自动录入地址信息再处理
                  select F_GET_DEFAULT_ORGBYUSER(IN_USERID, 0) into op_exec_org_id from dual;
                  select sys_guid() into op_id from dual;
                  select area_code into op_load_area_id from bas_area where area_cname=op_load_area_name and area_level=3 and rownum=1;
                  select area_code into op_load_area_id2 from bas_area where area_cname=op_load_area_name2 and area_level=4 and rownum=1;
                  load_code := substr(F_ZJM(op_load_address),0,32);
                  op_load_name := substr(op_load_address,0,100);
                  if op_load_area_name3 is not null then
                      select area_code into op_load_area_id3 from bas_area where area_cname=op_load_area_name3 and area_level=5 and rownum=1;
                  end if;

                  select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = load_code;
                  if r_count > 0 then
                      select id,addr_name,area_id,area_id2,area_id3 into op_id,op_load_name,op_load_area_id,op_load_area_id2,op_load_area_id3
                        from bas_address where customer_id = op_customer_id and addr_code = load_code;
                  else
                      --插入地址
                      insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
                        cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
                      values(op_id,load_code,op_customer_id,op_customer_name,'Y','N','N',op_load_area_id,op_load_area_name,op_load_name,op_load_address,
                        op_load_contact,op_load_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,IN_USERID,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3);
                  end if;
                  --exec_value := exec_value || ',''' || op_id || '''';

                  exec_sql := exec_sql || ',LOAD_ID,LOAD_CODE,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_ID2,LOAD_AREA_ID3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || load_code || ''',''' || op_load_name || ''',''' || op_load_area_id || ''','''
                     || op_load_area_id2 || ''',''' || op_load_area_id3 || '''';
              else
                  select count(1) into r_count from bas_address where addr_code = load_code;
                  if r_count > 1 then
                      select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = load_code;
                      if r_count > 1 or r_count = 0 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行收货方代码['|| unload_code ||']重复';
                          for h in 1..odr_lst.count loop
                              op_odr_no := odr_lst(h);
                              delete from trans_order_header where odr_no = op_odr_no;
                              delete from trans_order_item where odr_no = op_odr_no;
                          end loop;
                          commit;
                          return;
                      else
                          select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address
                          into op_id,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,tmp_load_contact,tmp_load_tel,tmp_load_address
                          from bas_address where customer_id = op_customer_id and addr_code = load_code;
                      end if;
                  else
                      select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address
                      into op_id,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,tmp_load_contact,tmp_load_tel,tmp_load_address
                      from bas_address where addr_code = load_code;
                  end if;

                  exec_sql := exec_sql || ',LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || op_load_name || ''',''' || op_load_area_id || ''',''' || op_load_area_name ||''','''
                     || op_load_area_id2 || ''',''' || op_load_area_name2 ||''',''' || op_load_area_id3 || ''',''' || op_load_area_name3 || '''';
                  if op_load_address is null then
                      exec_sql := exec_sql || ',LOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_load_address || '''';
                  end if;
                  if op_load_contact is null then
                      exec_sql := exec_sql || ',LOAD_CONTACT';
                      exec_value := exec_value || ',''' || tmp_load_contact || '''';
                  end if;
                  if op_load_tel is null then
                      exec_sql := exec_sql || ',LOAD_TEL';
                      exec_value := exec_value || ',''' || tmp_load_tel || '''';
                  end if;
              end if;
              --exec_value := exec_value || ',' || t_cur_value;
         elsif h.param_type = '5' then --收货地址特殊处理
              if unload_code is null then  --先自动录入地址信息再处理
                  select F_GET_DEFAULT_ORGBYUSER(IN_USERID, 0) into op_exec_org_id from dual;
                  select sys_guid() into op_id from dual;
                  select area_code into op_unload_area_id from bas_area where area_cname=op_unload_area_name and area_level=3 and rownum=1;
                  select area_code into op_unload_area_id2 from bas_area where area_cname=op_unload_area_name2 and area_level=4 and rownum=1;
                  unload_code := substr(F_ZJM(op_unload_address),0,32);
                  op_unload_name := substr(op_unload_address,0,100);
                  if op_unload_area_name3 is not null then
                      select area_code into op_unload_area_id3 from bas_area where area_cname=op_unload_area_name3 and area_level=5 and rownum=1;
                  end if;

                  select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                  if r_count > 0 then
                      select id,addr_name,area_id,area_id2,area_id3 into op_id,op_unload_name,op_unload_area_id,op_unload_area_id2,op_unload_area_id3
                      from bas_address where addr_code = unload_code and customer_id = op_customer_id;
                  else
                      --插入地址
                      insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
                        cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
                      values(op_id,unload_code,op_customer_id,op_customer_name,'N','N','Y',op_unload_area_id,op_unload_area_name,op_unload_name,op_unload_address,
                        op_unload_contact,op_unload_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,IN_USERID,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3);
                  end if;
                  --exec_value := exec_value || ',''' || op_id || '''';

                  exec_sql := exec_sql || ',UNLOAD_ID,UNLOAD_CODE,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_ID2,UNLOAD_AREA_ID3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || unload_code || ''',''' || op_unload_name || ''',''' || op_unload_area_id || ''','''
                     || op_unload_area_id2 || ''',''' || op_unload_area_id3 || '''';
              else
                  select count(1) into r_count from bas_address where addr_code = unload_code;
                  if r_count > 1 then
                      select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                      if r_count > 1 or r_count = 0 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行收货方代码['|| unload_code ||']重复';
                          for h in 1..odr_lst.count loop
                              op_odr_no := odr_lst(h);
                              delete from trans_order_header where odr_no = op_odr_no;
                              delete from trans_order_item where odr_no = op_odr_no;
                          end loop;
                          commit;
                          return;
                      else
                          select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,address,cont_name,cont_tel
                          into op_id,op_unload_name,op_unload_area_id,op_unload_area_name,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3,tmp_unload_address,tmp_unload_contact,tmp_unload_tel
                          from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                      end if;
                  else
                      select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,address,cont_name,cont_tel
                      into op_id,op_unload_name,op_unload_area_id,op_unload_area_name,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3,tmp_unload_address,tmp_unload_contact,tmp_unload_tel
                      from bas_address where addr_code = unload_code;
                  end if;
                  exec_sql := exec_sql || ',UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || op_unload_name || ''',''' || op_unload_area_id || ''',''' || op_unload_area_name || ''','''
                     || op_unload_area_id2 || ''',''' || op_unload_area_name2 || ''',''' || op_unload_area_id3 || ''',''' || op_unload_area_name3 || '''';
                  if op_unload_address is null then
                      exec_sql := exec_sql || ',UNLOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_unload_address || '''';
                  end if;
                  if op_unload_contact is null then
                      exec_sql := exec_sql || ',UNLOAD_CONTACT';
                      exec_value := exec_value || ',''' || tmp_unload_contact || '''';
                  end if;
                  if op_unload_tel is null then
                      exec_sql := exec_sql || ',UNLOAD_TEL';
                      exec_value := exec_value || ',''' || tmp_unload_tel || '''';
                  end if;
              end if;
              --exec_value := exec_value || ',' || t_cur_value;
         end if;
         <<next_loop>>
             OUTPUT_RESULLT := '';
      end loop;
      select nvl(max(odr_row),0) + 1 into op_odr_row from trans_order_item where odr_no = op_odr_no;
      exec_sql := 'INSERT INTO TRANS_ORDER_ITEM(ODR_NO,ODR_ROW,ADDWHO,PACK_ID,ADDTIME' || exec_sql || ') VALUES(''' || op_odr_no || ''',' || op_odr_row || ',''' || IN_USERID || ''',''' || op_packid ||''',sysdate' || exec_value || ')';
      execute immediate exec_sql;

      ORDER_QTY_COUNT(op_odr_no, OUTPUT_RESULLT); --汇总订单信息
      --更新订单发货点和收货点信息
      UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select min(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select max(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;

      OUTPUT_RESULLT := '00';
  end loop;
  close C_ACT;
  commit;

  delete from TMP_ORDER_IMPORT WHERE TMPID = IN_TMPID;
  /*for h in 1..odr_lst.count loop
      op_odr_no := odr_lst(h);
      ORDER_QTY_COUNT(op_odr_no, OUTPUT_RESULLT); --汇总订单信息
      --更新订单发货点和收货点信息
      UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select min(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select max(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
  end loop;*/
  OUTPUT_RESULLT := '00';
  commit;
  EXCEPTION
    WHEN OTHERS THEN
        --OUTPUT_RESULLT :='01'||sqlerrm || ']';
        OUTPUT_RESULLT := '01' || OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列值为[' || t_cur_value || '],数据异常请联系管理员!<br />';
        rollback;
        t_msg := substr(sqlerrm,1,200);
        insert into t_input_excel_temp(type_name,VALUE,line,column_name,column_cname,id,custom_odr_no)
        values(tmp_param_type,tmp_default_value,r_LINE,tmp_column,t_msg,to_char(sysdate,'YYYY-MM-DD HH24:MI'),tmp_table);

        delete from TMP_ORDER_IMPORT WHERE TMPID = IN_TMPID;
        for h in 1..odr_lst.count loop
            op_odr_no := odr_lst(h);
            delete from trans_order_header where odr_no = op_odr_no;
            delete from trans_order_item where odr_no = op_odr_no;
        end loop;
        commit;
        --RAISE_APPLICATION_ERROR(-20101, '执行全局校验时抛出异常: '||sqlerrm, TRUE);
END;

/

--caijiante 2017-05-16
--SYS_FUNC_PAGE 添加4条记录
964?	22965	P06_T051_P1_03	P06_T051_P1	新增	150	Y	2015/5/27	wpsadmin	新增	B	
965?	22966	P06_T051_P1_04	P06_T051_P1	保存	150	Y	2015/5/27	wpsadmin	保存	B	
966?	22967	P06_T051_P1_05	P06_T051_P1	删除	150	Y	2015/5/27	wpsadmin	删除	B	
967?	22968	P06_T051_P1_06	P06_T051_P1	取消	150	Y	2015/5/27	wpsadmin	取消	B	


--yuanlei 2017-5-16
create or replace procedure SP_IMPORT_ORDER(
IN_TMPID varchar2,
IN_USERID varchar2,
IN_LANGUAGE VARCHAR2,
OUTPUT_RESULLT in out varchar2)
IS

t_cur_column varchar2(100);
t_cur_value varchar2(500);
t_cur_name varchar2(100);
t_msg varchar2(200);
t_verify_type number(4);
t_table_name varchar2(100);
t_verify_code varchar2(100);

v_sql varchar2(2048);
temp_sql varchar2(1024);
temp_result varchar2(500);
tmp_verify_code varchar2(100);

r_REFENENCE1 varchar2(100);
r_CUSTOM_ODR_NO varchar2(100);
r_CUSTOMER_NAME varchar2(100);
r_BIZ_TYP varchar2(100);
r_ODR_TYP varchar2(100);
r_ODR_TIME varchar2(100);
r_VEHICLE_TYP varchar2(100);
r_TEMPERATURE1 varchar2(100);
r_TEMPERATURE2 varchar2(100);
r_PRE_LOAD_TIME varchar2(100);
r_PRE_UNLOAD_TIME varchar2(100);
r_SLF_PICKUP_FLAG varchar2(10);
r_SLF_DELIVER_FLAG varchar2(10);
r_NOTES varchar2(500);
r_LOAD_CODE varchar2(100);
r_LOAD_AREA_NAME varchar2(100);
r_LOAD_AREA_NAME2 varchar2(100);
r_LOAD_AREA_NAME3 varchar2(100);
r_LOAD_ADDRESS varchar2(300);
r_LOAD_CONTACT varchar2(100);
r_LOAD_TEL varchar2(100);
r_UNLOAD_CODE varchar2(100);
r_UNLOAD_AREA_NAME varchar2(100);
r_UNLOAD_AREA_NAME2 varchar2(100);
r_UNLOAD_AREA_NAME3 varchar2(100);
r_UNLOAD_ADDRESS varchar2(300);
r_UNLOAD_CONTACT varchar2(100);
r_UNLOAD_TEL varchar2(100);
r_SKU varchar2(100);
r_SKU_NAME varchar2(200);
r_UOM varchar2(100);
r_QNTY varchar2(100);
r_TEMPERATURE varchar2(100);
r_G_WGT varchar2(100);
r_VOL varchar2(100);
r_LINE number(4);
r_USERID varchar2(30);
r_BTCH_NUM VARCHAR2(50);

type   cur_t   is   ref   cursor;
      C_ACT   cur_t;

TYPE array_var IS VARRAY(37) OF nvarchar2(10000);

tmp_key array_var;
tmp_name array_var;
tmp_value array_var;

tmp_table varchar2(255);
tmp_column varchar2(255);
tmp_param_type NUMBER;
tmp_replace_field varchar2(100);
tmp_default_value varchar2(255);

exec_sql varchar2(4096);
exec_value varchar2(4096);

r_count number(4);
pos number(4);

load_code varchar2(100);
unload_code varchar2(100);

op_customer_id varchar2(32);
op_customer_name VARCHAR2(50);
op_load_area_name varchar2(50);
op_load_area_name2 varchar2(50);
op_load_area_name3 varchar2(50);
op_unload_area_name varchar2(50);
op_unload_area_name2 varchar2(50);
op_unload_area_name3 varchar2(50);
op_load_address varchar2(300);
op_load_contact varchar2(40);
op_load_tel varchar2(80);
op_unload_address varchar2(300);
op_unload_contact varchar2(40);
op_unload_tel varchar2(80);
op_load_area_id varchar2(32);
op_load_area_id2 varchar2(32);
op_load_area_id3 varchar2(32);
op_load_name varchar2(100);
op_unload_area_id varchar2(32);
op_unload_area_id2 varchar2(32);
op_unload_area_id3 varchar2(32);
op_unload_name varchar2(100);
op_exec_org_id varchar2(32);
op_id varchar2(32);
op_odr_no varchar2(100);
op_odr_row number(4);
op_skuname varchar2(100);
op_packid varchar2(32);

odr_lst LST := LST();

tmp_unload_address varchar2(300);
tmp_unload_contact varchar2(40);
tmp_unload_tel varchar2(40);
tmp_load_address  varchar2(300);
tmp_load_contact varchar2(40);
tmp_load_tel varchar2(80);

i_key LST := LST();
i_value LST:= LST();
i_size number(4);
t_key varchar2(100);
t_value varchar2(100);
c_area_name char(1);
c_area_name2 char(1);
c_area_name3 char(1);
c_address char(1);
c_contact char(1);
c_tel char(1);

CURSOR verify is
      select verify_type,table_name,verify_code,error_msg from t_input_excel_verify where type_name = 'TMP_ORDER_IMPORT' and COLUMN_NAME = t_cur_column;
CURSOR head is
      select column_name,default_value,param_type,replace_field,table_name from t_input_excel_default where table_name = 'TRANS_ORDER_HEADER' order by exec_seq asc;
CURSOR item is
      select column_name,default_value,param_type,replace_field,table_name from t_input_excel_default where table_name = 'TRANS_ORDER_ITEM' order by exec_seq asc;
--IN_TMPID varchar2(32);
BEGIN
  --IN_TMPID := '1489116521217';
  OUTPUT_RESULLT := '';
  --return;
  tmp_key := array_var('REFENENCE1','CUSTOM_ODR_NO','CUSTOMER_NAME','BIZ_TYP','ODR_TYP','ODR_TIME','VEHICLE_TYP','TEMPERATURE1','TEMPERATURE2','SLF_PICKUP_FLAG'
  ,'SLF_DELIVER_FLAG','NOTES','PRE_LOAD_TIME','PRE_UNLOAD_TIME','LOAD_CODE','LOAD_AREA_NAME','LOAD_AREA_NAME2','LOAD_AREA_NAME3','LOAD_ADDRESS','LOAD_CONTACT'
  ,'LOAD_TEL','UNLOAD_CODE','UNLOAD_AREA_NAME','UNLOAD_AREA_NAME2','UNLOAD_AREA_NAME3','UNLOAD_ADDRESS','UNLOAD_CONTACT','UNLOAD_TEL','SKU','SKU_NAME','UOM'
  ,'QNTY','TEMPERATURE','G_WGT','VOL','BTCH_NUM','USER_ID');
  
  tmp_name := array_var('运单号','客户订单号','客户代码','业务类型','运输类型','下单时间','车辆类型','温度下限','温度上限','客户自提'
  ,'客户自送','备注','要求发货时间','要求到货时间','发货点代码','发货省','发货市','发货区','发货详细地址','发货联系人'
  ,'发货联系电话','卸货点代码','卸货省','卸货市','卸货区','卸货详细地址','卸货联系人','卸货联系电话','货品代码','货品名称','运输单位'
  ,'数量','温区','毛重(千克)','体积（立方）','订单组号','登录用户');
  v_sql := '';
  r_LINE := 1;
  pos := 1;
  for i in 1..tmp_key.count loop
      t_cur_column := tmp_key(i);
      v_sql := v_sql|| t_cur_column || ',';
  end loop;
  v_sql := 'SELECT ' || v_sql || 'LINENO FROM TMP_ORDER_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' ORDER BY LINENO ASC';
  --校验字段
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;

      tmp_value := array_var(r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID);

      if r_CUSTOMER_NAME is null then  --当客户单号为空时，可认为与上一行信息相同，直接跳到明细列进行校验。
          pos := 15;
      end if;

      for i in pos..tmp_key.count loop
           t_cur_column := tmp_key(i);
           t_cur_value := tmp_value(i);
           t_cur_name := tmp_name(i);
           for m in verify loop
               t_verify_type := m.verify_type;
               t_table_name := m.table_name;
               t_verify_code := m.verify_code;
               t_msg := m.error_msg;

               if t_verify_type = 0 then  --非空检验
                   if t_cur_value is null or length(t_cur_value) < 1 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   end if;
               elsif t_verify_type = 1 and length(t_cur_value) > 0 then --唯一性校验
                   temp_sql := 'select count(1) from ' || t_table_name || ' where ' || t_cur_column || '=''' || t_cur_value || '''';
                   execute immediate temp_sql into r_count;
                   if r_count > 0 then
                        OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                        if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                        end if;
                   else
                      temp_sql := 'select count(1) from TMP_ORDER_IMPORT where TMPID = ''' || IN_TMPID || ''' AND USER_ID = '''
                        || IN_USERID || ''' and ' || t_cur_column || '= ''' || t_cur_value || '''';
                      execute immediate temp_sql into r_count;
                      if r_count > 1 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                          if length(OUTPUT_RESULLT) > 1000 then
                               OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                               return;
                           end if;
                      end if;
                   end if;
               elsif t_verify_type = 2 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列值为[' || t_cur_value || '],' || t_msg;
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   else
                      if t_cur_column = 'SKU' then  --如果是只填写SKU，则将SKU对应的名称写到临时表，避免校验SKU_NAME时因为无值不通过
                          select sku_cname into op_skuname from BAS_SKU where sku = t_cur_value;
                          update TMP_ORDER_IMPORT set SKU_NAME = op_skuname
                            WHERE TMPID = IN_TMPID AND USER_ID = IN_USERID AND LINENO = r_LINE;
                          tmp_value(30) := op_skuname;
                      end if;
                   end if;
               elsif t_verify_type = 3 then   --二选一非空校验
                   if t_cur_value is null then
                       select verify_code into tmp_verify_code from t_input_excel_verify where column_name = t_verify_code and verify_type = '2' and type_name = 'TMP_ORDER_IMPORT';
                       temp_sql := 'select ' || t_verify_code || ' from TMP_ORDER_IMPORT WHERE TMPID = ''' || IN_TMPID || ''' AND USER_ID = ''' || IN_USERID || ''' and LINENO = ''' || r_LINE || '''';
                       execute immediate temp_sql into temp_result;
                       temp_sql := REPLACE(tmp_verify_code, '?', temp_result);
                       execute immediate temp_sql into r_count;
                       if r_count = 0 then
                           OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                           if length(OUTPUT_RESULLT) > 1000 then
                               OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                               return;
                           end if;
                       end if;
                   end if;
               elsif t_verify_type = 4 and length(t_verify_code) > 0 and length(t_cur_value) > 0 then --长度校验
                   temp_sql := REPLACE(t_verify_code, '?', t_cur_value);
                   execute immediate temp_sql into r_count;
                   if r_count = 0 then
                       OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列' || t_msg;
                       if length(OUTPUT_RESULLT) > 1000 then
                           OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
                           return;
                       end if;
                   end if;
               end if;
           end loop;
      end loop;
      <<next_loop>>
          t_cur_column := '';
  end loop;
  close C_ACT;

  if length(OUTPUT_RESULLT) > 0 then
    OUTPUT_RESULLT:='01'||OUTPUT_RESULLT;
    rollback;
    -- 失败删除中间表
    delete from TMP_ORDER_IMPORT where TMPID = IN_TMPID;
    commit;
    return;
  end if;
  --插入数据
  Open  C_ACT   for   v_sql ;
  loop
      fetch C_ACT into r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID,r_LINE;            --V418A
      exit when C_ACT%notfound;

      tmp_value := array_var(r_REFENENCE1,r_CUSTOM_ODR_NO,r_CUSTOMER_NAME,r_BIZ_TYP,r_ODR_TYP
       ,r_ODR_TIME,r_VEHICLE_TYP,r_TEMPERATURE1,r_TEMPERATURE2,r_SLF_PICKUP_FLAG,r_SLF_DELIVER_FLAG
       ,r_NOTES,r_PRE_LOAD_TIME,r_PRE_UNLOAD_TIME,r_LOAD_CODE
       ,r_LOAD_AREA_NAME,r_LOAD_AREA_NAME2,r_LOAD_AREA_NAME3,r_LOAD_ADDRESS,r_LOAD_CONTACT
       ,r_LOAD_TEL,r_UNLOAD_CODE,r_UNLOAD_AREA_NAME,r_UNLOAD_AREA_NAME2,r_UNLOAD_AREA_NAME3
       ,r_UNLOAD_ADDRESS,r_UNLOAD_CONTACT,r_UNLOAD_TEL,r_SKU,r_SKU_NAME
       ,r_UOM,r_QNTY,r_TEMPERATURE,r_G_WGT,r_VOL,r_BTCH_NUM,r_USERID);

      exec_sql := '';
      exec_value := '';
      i_size := 1;
      i_key := LST();
      i_value := LST();
      if r_CUSTOMER_NAME is not null then
          for h in head loop
              tmp_table := h.table_name;
              tmp_column := h.column_name;
              tmp_param_type := h.param_type;
              tmp_replace_field := h.replace_field;
              tmp_default_value := h.default_value;
              --exec_sql := exec_sql || ',' || h.column_name;
              
              i_key.extend();
              i_key(i_size) := h.column_name;
              if h.param_type = '0' then  --定制
                  i_value.extend();
                  i_value(i_size) := h.default_value;
                  --exec_value := exec_value || ',' || h.default_value;
              elsif h.param_type = '1' then  --从执行结果取值
                  temp_sql := h.default_value;
                  execute immediate temp_sql into temp_result;
                  --exec_value := exec_value || ',''' || temp_result || '''';
                  i_value.extend();
                  i_value(i_size) := temp_result;
              elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
                  for i in 1..tmp_key.count loop
                      t_cur_column := tmp_key(i);
                      if t_cur_column = h.Replace_Field then
                          t_cur_value := tmp_value(i);
                          exit;
                      end if;
                  end loop;
                  if t_cur_value is not null then
                      temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                      execute immediate temp_sql into temp_result;
                      --exec_value := exec_value || ',''' || temp_result || '''';
                      i_value.extend();
                      i_value(i_size) := temp_result;
                  else
                      --exec_value := exec_value || ',NULL';
                      i_value.extend();
                      i_value(i_size) := NULL;
                  end if;
                  if  h.column_name = 'CUSTOMER_ID' then
                      op_customer_id := temp_result;
                  elsif h.column_name = 'CUSTOMER_NAME' then
                      op_customer_name := temp_result;
                  end if;
              elsif h.param_type = '3' then  --直接从临时表上取值
                  for i in 1..tmp_key.count loop
                      t_cur_column := tmp_key(i);
                      if t_cur_column = h.column_name then
                          t_cur_value := tmp_value(i);
                          exit;
                      end if;
                  end loop;
                  if t_cur_value is null and h.column_name = 'CUSTOMER_NAME' then
                     goto next_loop;
                  end if;
                  if t_cur_value is null then
                      --exec_value := exec_value || ',NULL';
                      i_value.extend();
                      i_value(i_size) := NULL;
                  else
                      --exec_value := exec_value || ',''' || t_cur_value || '''';
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                  end if;
             end if;
             i_size := i_size + 1;
          end loop;
      end if;
      <<next_loop>>
          OUTPUT_RESULLT := '';
      if i_key.count > 0 then
          --获取托运单号
          SP_GET_IDSEQ('ORDER',op_odr_no);

          exec_sql := '';
          exec_value := '';
          for i in 1..i_key.count loop
              t_key := i_key(i);
              t_value := i_value(i);
              exec_sql := exec_sql || ',' || t_key;
              if t_value is null then 
                  exec_value := exec_value || ',NULL';
              else
                  exec_value := exec_value || ','''|| t_value || '''';
              end if;
          end loop;
          exec_sql := 'INSERT INTO TRANS_ORDER_HEADER(ODR_NO,ADDWHO,ADDTIME' || exec_sql || ') VALUES(''' || op_odr_no || ''',''' || IN_USERID || ''',sysdate' || exec_value || ')';
          execute immediate exec_sql;

          odr_lst.extend();
          odr_lst(odr_lst.count) := op_odr_no;
      end if;
      exec_sql := '';
      exec_value := '';
      op_packid := '2EEB495B515049B3A9CD76572DDDC999';
      i_key := LST();
      i_value := LST();
      i_size := 1;
      for h in item loop
          tmp_table := h.table_name;
          tmp_column := h.column_name;
          tmp_param_type := h.param_type;
          tmp_replace_field := h.replace_field;
          tmp_default_value := h.default_value;
          if h.param_type = '0' then  --定制
              exec_sql := exec_sql || ',' || h.column_name;
              exec_value := exec_value || ',' || h.default_value;
              i_key.extend();
              i_key(i_size) := h.column_name;
              i_value.extend();
              i_value(i_size) := h.default_value;
              i_size := i_size + 1;
          elsif h.param_type = '1' then  --从执行结果取值
              exec_sql := exec_sql || ',' || h.column_name;
              temp_sql := h.default_value;
              execute immediate temp_sql into temp_result;
              exec_value := exec_value || ',''' || temp_result || '''';
              i_key.extend();
              i_key(i_size) := h.column_name;
              i_value.extend();
              i_value(i_size) := temp_result;
              i_size := i_size + 1;
          elsif h.param_type = '2' then   --将？替换成参数值，并从结果取值
              exec_sql := exec_sql || ',' || h.column_name;
              i_key.extend();
              i_key(i_size) := h.column_name;
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.replace_field then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if t_cur_value is not null then
                  temp_sql := REPLACE(h.default_value, '?', t_cur_value);
                  execute immediate temp_sql into temp_result;
                  exec_value := exec_value || ',''' || temp_result || '''';
                  i_value.extend();
                  i_value(i_size) := temp_result;
              else
                  exec_value := exec_value || ',NULL';
                  i_value.extend();
                  i_value(i_size) := NULL;
              end if;
              i_size := i_size + 1;
          elsif h.param_type = '3' then  --直接从临时表上取值
              for i in 1..tmp_key.count loop
                  t_cur_column := tmp_key(i);
                  if t_cur_column = h.column_name then
                      t_cur_value := tmp_value(i);
                      exit;
                  end if;
              end loop;
              if h.column_name = 'LOAD_CODE' then
                  load_code := t_cur_value;
                  if load_code is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'UNLOAD_CODE' then
                  unload_code := t_cur_value;
                  if unload_code is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME' then
                  op_unload_area_name := t_cur_value;
                  if op_unload_area_name is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME2' then
                  op_unload_area_name2 := t_cur_value;
                  if op_unload_area_name2 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value  || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'UNLOAD_AREA_NAME3' then
                  op_unload_area_name3 := t_cur_value;
                  if op_unload_area_name3 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'UNLOAD_ADDRESS' then
                  op_unload_address := t_cur_value;
                  if op_unload_address is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'UNLOAD_CONTACT' then
                  op_unload_contact := t_cur_value;
                  if op_unload_contact is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'UNLOAD_TEL' then
                  op_unload_tel := t_cur_value;
                  if op_unload_tel is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME' then
                  op_load_area_name := t_cur_value;
                  if op_load_area_name is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME2' then
                  op_load_area_name2 := t_cur_value;
                  if op_load_area_name2 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'LOAD_AREA_NAME3' then
                  op_load_area_name3 := t_cur_value;
                  if op_load_area_name3 is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'LOAD_ADDRESS' then
                  op_load_address := t_cur_value;
                  if op_load_address is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'LOAD_CONTACT' then
                  op_load_contact := t_cur_value;
                  if op_load_contact is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'LOAD_TEL' then
                  op_load_tel := t_cur_value;
                  if op_load_tel is not null then
                      exec_sql := exec_sql || ',' || h.column_name;
                      exec_value := exec_value || ',''' || t_cur_value || '''';
                      
                      i_key.extend();
                      i_key(i_size) := h.column_name;
                      i_value.extend();
                      i_value(i_size) := t_cur_value;
                      i_size := i_size + 1;
                  end if;
              elsif h.column_name = 'SKU' then
                  if t_cur_value is not null then
                      select id into op_packid from bas_sku where sku = t_cur_value;
                  end if;
                  exec_sql := exec_sql || ',' || h.column_name;
                  exec_value := exec_value || ',''' || t_cur_value || '''';
                  
                  i_key.extend();
                  i_key(i_size) := h.column_name;
                  i_value.extend();
                  i_value(i_size) := t_cur_value;
                  i_size := i_size + 1;
              else
                  exec_sql := exec_sql || ',' || h.column_name;
                  exec_value := exec_value || ',''' || t_cur_value || '''';
                  
                  i_key.extend();
                  i_key(i_size) := h.column_name;
                  i_value.extend();
                  i_value(i_size) := t_cur_value;
                  i_size := i_size + 1;
              end if;
         elsif h.param_type = '4' then --发货地址特殊处理
              if load_code is null then  --先自动录入地址信息再处理
                  select F_GET_DEFAULT_ORGBYUSER(IN_USERID, 0) into op_exec_org_id from dual;
                  select sys_guid() into op_id from dual;
                  select area_code into op_load_area_id from bas_area where area_cname=op_load_area_name and area_level=3 and rownum=1;
                  select area_code into op_load_area_id2 from bas_area where area_cname=op_load_area_name2 and area_level=4 and rownum=1;
                  load_code := substr(F_ZJM(op_load_address),0,32);
                  op_load_name := substr(op_load_address,0,100);
                  if op_load_area_name3 is not null then
                      select area_code into op_load_area_id3 from bas_area where area_cname=op_load_area_name3 and area_level=5 and rownum=1;
                  end if;

                  select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = load_code;
                  if r_count > 0 then
                      select id,addr_name,area_id,area_id2,area_id3 into op_id,op_load_name,op_load_area_id,op_load_area_id2,op_load_area_id3
                        from bas_address where customer_id = op_customer_id and addr_code = load_code;
                  else
                      --插入地址
                      insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
                        cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
                      values(op_id,load_code,op_customer_id,op_customer_name,'Y','N','N',op_load_area_id,op_load_area_name,op_load_name,op_load_address,
                        op_load_contact,op_load_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,IN_USERID,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3);
                  end if;

                  exec_sql := exec_sql || ',LOAD_ID,LOAD_CODE,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_ID2,LOAD_AREA_ID3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || load_code || ''',''' || op_load_name || ''',''' || op_load_area_id || ''','''
                     || op_load_area_id2 || ''',''' || op_load_area_id3 || '''';
                  i_key.extend();
                  i_key(i_size) := 'LOAD_ID';
                  i_value.extend();
                  i_value(i_size) := op_id;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'LOAD_CODE';
                  i_value.extend();
                  i_value(i_size) := load_code;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'LOAD_NAME';
                  i_value.extend();
                  i_value(i_size) := op_load_name;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'LOAD_AREA_ID';
                  i_value.extend();
                  i_value(i_size) := op_load_area_id;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'LOAD_AREA_ID2';
                  i_value.extend();
                  i_value(i_size) := op_load_area_id2;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'LOAD_AREA_ID2';
                  i_value.extend();
                  i_value(i_size) := op_load_area_id2;
                  i_size := i_size + 1;
              else
                  select count(1) into r_count from bas_address where addr_code = load_code;
                  if r_count > 1 then
                      select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = load_code;
                      if r_count > 1 or r_count = 0 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行收货方代码['|| unload_code ||']重复';
                          for h in 1..odr_lst.count loop
                              op_odr_no := odr_lst(h);
                              delete from trans_order_header where odr_no = op_odr_no;
                              delete from trans_order_item where odr_no = op_odr_no;
                          end loop;
                          commit;
                          return;
                      else
                          select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address
                          into op_id,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,tmp_load_contact,tmp_load_tel,tmp_load_address
                          from bas_address where customer_id = op_customer_id and addr_code = load_code;
                      end if;
                  else
                      select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address
                      into op_id,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,tmp_load_contact,tmp_load_tel,tmp_load_address
                      from bas_address where addr_code = load_code;
                  end if;

                  exec_sql := exec_sql || ',LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || op_load_name || ''',''' || op_load_area_id || ''',''' || op_load_area_name ||''','''
                     || op_load_area_id2 || ''',''' || op_load_area_name2 ||''',''' || op_load_area_id3 || ''',''' || op_load_area_name3 || '''';
                  
                  i_key.extend();
                  i_key(i_size) := 'LOAD_ID';
                  i_value.extend();
                  i_value(i_size) := op_id;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'LOAD_NAME';
                  i_value.extend();
                  i_value(i_size) := op_load_name;
                  i_size := i_size + 1;      
                  i_key.extend();
                  i_key(i_size) := 'LOAD_AREA_ID';
                  i_value.extend();
                  i_value(i_size) := op_load_area_id;
                  i_size := i_size + 1;   
                  i_key.extend();
                  i_key(i_size) := 'LOAD_AREA_ID2';
                  i_value.extend();
                  i_value(i_size) := op_load_area_id2;
                  i_size := i_size + 1;  
                  i_key.extend();
                  i_key(i_size) := 'LOAD_AREA_ID3';
                  i_value.extend();
                  i_value(i_size) := op_load_area_id3;
                  i_size := i_size + 1;
                  
                  c_area_name := 'N';
                  c_area_name2 := 'N';
                  c_area_name3 := 'N';
                  c_address := 'N';
                  c_contact := 'N';
                  c_tel := 'N';
                  for i in 1..i_key.count loop
                      t_key := i_key(i);
                      if t_key = 'LOAD_AREA_NAME' then
                          c_area_name := 'Y';
                          i_value(i) := op_load_area_name;
                      elsif t_key = 'LOAD_AREA_NAME2' then
                          c_area_name2 := 'Y';
                          i_value(i) := op_load_area_name2;
                      elsif t_key = 'LOAD_AREA_NAME3' then
                          c_area_name3 := 'Y';
                          i_value(i) := op_load_area_name3;
                      elsif t_key = 'LOAD_ADDRESS' then
                          c_address := 'Y';
                      elsif t_key = 'LOAD_CONTACT' then
                          c_contact := 'Y';
                      elsif t_key = 'LOAD_TEL' then
                          c_tel := 'Y';
                      end if;
                  end loop;
                  
                  if c_area_name = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'LOAD_AREA_NAME';
                      i_value.extend();
                      i_value(i_size) := op_load_area_name;
                      i_size := i_size + 1;
                  end if;
                  if c_area_name2 = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'LOAD_AREA_NAME2';
                      i_value.extend();
                      i_value(i_size) := op_load_area_name2;
                      i_size := i_size + 1;
                  end if;
                  if c_area_name3 = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'LOAD_AREA_NAME3';
                      i_value.extend();
                      i_value(i_size) := op_load_area_name3;
                      i_size := i_size + 1;
                  end if;
                  if c_address = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'LOAD_ADDRESS';
                      i_value.extend();
                      i_value(i_size) := tmp_load_address;
                      i_size := i_size + 1;
                  end if;
                  if c_contact = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'LOAD_CONTACT';
                      i_value.extend();
                      i_value(i_size) := tmp_load_contact;
                      i_size := i_size + 1;
                  end if;
                  if c_tel = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'LOAD_TEL';
                      i_value.extend();
                      i_value(i_size) := tmp_load_tel;
                      i_size := i_size + 1;
                  end if;
                  /*if op_load_address is null then
                      exec_sql := exec_sql || ',LOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_load_address || '''';
                      i_size := i_size + 1;
                      
                  end if;
                  if op_load_contact is null then
                      exec_sql := exec_sql || ',LOAD_CONTACT';
                      exec_value := exec_value || ',''' || tmp_load_contact || '''';
                  end if;
                  if op_load_tel is null then
                      exec_sql := exec_sql || ',LOAD_TEL';
                      exec_value := exec_value || ',''' || tmp_load_tel || '''';
                  end if;*/
       
                    
              end if;
              --exec_value := exec_value || ',' || t_cur_value;
         elsif h.param_type = '5' then --收货地址特殊处理
              if unload_code is null then  --先自动录入地址信息再处理
                  select F_GET_DEFAULT_ORGBYUSER(IN_USERID, 0) into op_exec_org_id from dual;
                  select sys_guid() into op_id from dual;
                  select area_code into op_unload_area_id from bas_area where area_cname=op_unload_area_name and area_level=3 and rownum=1;
                  select area_code into op_unload_area_id2 from bas_area where area_cname=op_unload_area_name2 and area_level=4 and rownum=1;
                  unload_code := substr(F_ZJM(op_unload_address),0,32);
                  op_unload_name := substr(op_unload_address,0,100);
                  if op_unload_area_name3 is not null then
                      select area_code into op_unload_area_id3 from bas_area where area_cname=op_unload_area_name3 and area_level=5 and rownum=1;
                  end if;

                  select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                  if r_count > 0 then
                      select id,addr_name,area_id,area_id2,area_id3 into op_id,op_unload_name,op_unload_area_id,op_unload_area_id2,op_unload_area_id3
                      from bas_address where addr_code = unload_code and customer_id = op_customer_id;
                  else
                      --插入地址
                      insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
                        cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
                      values(op_id,unload_code,op_customer_id,op_customer_name,'N','N','Y',op_unload_area_id,op_unload_area_name,op_unload_name,op_unload_address,
                        op_unload_contact,op_unload_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,IN_USERID,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3);
                  end if;
                  --exec_value := exec_value || ',''' || op_id || '''';

                  exec_sql := exec_sql || ',UNLOAD_ID,UNLOAD_CODE,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_ID2,UNLOAD_AREA_ID3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || unload_code || ''',''' || op_unload_name || ''',''' || op_unload_area_id || ''','''
                     || op_unload_area_id2 || ''',''' || op_unload_area_id3 || '''';
                     
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_ID';
                  i_value.extend();
                  i_value(i_size) := op_id;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_CODE';
                  i_value.extend();
                  i_value(i_size) := unload_code;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_NAME';
                  i_value.extend();
                  i_value(i_size) := op_unload_name;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_AREA_ID';
                  i_value.extend();
                  i_value(i_size) := op_unload_area_id;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_AREA_ID2';
                  i_value.extend();
                  i_value(i_size) := op_unload_area_id2;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_AREA_ID3';
                  i_value.extend();
                  i_value(i_size) := op_unload_area_id3;
                  i_size := i_size + 1;
              else
                  select count(1) into r_count from bas_address where addr_code = unload_code;
                  if r_count > 1 then
                      select count(1) into r_count from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                      if r_count > 1 or r_count = 0 then
                          OUTPUT_RESULLT := OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行收货方代码['|| unload_code ||']重复';
                          for h in 1..odr_lst.count loop
                              op_odr_no := odr_lst(h);
                              delete from trans_order_header where odr_no = op_odr_no;
                              delete from trans_order_item where odr_no = op_odr_no;
                          end loop;
                          commit;
                          return;
                      else
                          select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,address,cont_name,cont_tel
                          into op_id,op_unload_name,op_unload_area_id,op_unload_area_name,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3,tmp_unload_address,tmp_unload_contact,tmp_unload_tel
                          from bas_address where customer_id = op_customer_id and addr_code = unload_code;
                      end if;
                  else
                      select id,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,address,cont_name,cont_tel
                      into op_id,op_unload_name,op_unload_area_id,op_unload_area_name,op_unload_area_id2,op_unload_area_name2,op_unload_area_id3,op_unload_area_name3,tmp_unload_address,tmp_unload_contact,tmp_unload_tel
                      from bas_address where addr_code = unload_code;
                  end if;
                  exec_sql := exec_sql || ',UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3';
                  exec_value := exec_value || ',''' || op_id || ''',''' || op_unload_name || ''',''' || op_unload_area_id || ''',''' || op_unload_area_name || ''','''
                     || op_unload_area_id2 || ''',''' || op_unload_area_name2 || ''',''' || op_unload_area_id3 || ''',''' || op_unload_area_name3 || '''';
                     
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_ID';
                  i_value.extend();
                  i_value(i_size) := op_id;
                  i_size := i_size + 1;
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_NAME';
                  i_value.extend();
                  i_value(i_size) := op_unload_name;
                  i_size := i_size + 1;      
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_AREA_ID';
                  i_value.extend();
                  i_value(i_size) := op_unload_area_id;
                  i_size := i_size + 1;   
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_AREA_ID2';
                  i_value.extend();
                  i_value(i_size) := op_unload_area_id2;
                  i_size := i_size + 1;  
                  i_key.extend();
                  i_key(i_size) := 'UNLOAD_AREA_ID3';
                  i_value.extend();
                  i_value(i_size) := op_unload_area_id3;
                  i_size := i_size + 1;
                  
                  c_area_name := 'N';
                  c_area_name2 := 'N';
                  c_area_name3 := 'N';
                  c_address := 'N';
                  c_contact := 'N';
                  c_tel := 'N';
                  for i in 1..i_key.count loop
                      t_key := i_key(i);
                      if t_key = 'UNLOAD_AREA_NAME' then
                          c_area_name := 'Y';
                          i_value(i) := op_unload_area_name;
                      elsif t_key = 'UNLOAD_AREA_NAME2' then
                          c_area_name2 := 'Y';
                          i_value(i) := op_unload_area_name2;
                      elsif t_key = 'UNLOAD_AREA_NAME3' then
                          c_area_name3 := 'Y';
                          i_value(i) := op_unload_area_name3;
                      elsif t_key = 'UNLOAD_ADDRESS' then
                          c_address := 'Y';
                      elsif t_key = 'UNLOAD_CONTACT' then
                          c_contact := 'Y';
                      elsif t_key = 'UNLOAD_TEL' then
                          c_tel := 'Y';
                      end if;
                  end loop;
                  
                  if c_area_name = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'UNLOAD_AREA_NAME';
                      i_value.extend();
                      i_value(i_size) := op_unload_area_name;
                      i_size := i_size + 1;
                  end if;
                  if c_area_name2 = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'UNLOAD_AREA_NAME2';
                      i_value.extend();
                      i_value(i_size) := op_unload_area_name2;
                      i_size := i_size + 1;
                  end if;
                  if c_area_name3 = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'UNLOAD_AREA_NAME3';
                      i_value.extend();
                      i_value(i_size) := op_unload_area_name3;
                      i_size := i_size + 1;
                  end if;
                  if c_address = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'UNLOAD_ADDRESS';
                      i_value.extend();
                      i_value(i_size) := tmp_unload_address;
                      i_size := i_size + 1;
                  end if;
                  if c_contact = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'UNLOAD_CONTACT';
                      i_value.extend();
                      i_value(i_size) := tmp_unload_contact;
                      i_size := i_size + 1;
                  end if;
                  if c_tel = 'N' then
                      i_key.extend();
                      i_key(i_size) := 'UNLOAD_TEL';
                      i_value.extend();
                      i_value(i_size) := tmp_unload_tel;
                      i_size := i_size + 1;
                  end if;
                  /*if op_load_address is null then
                      exec_sql := exec_sql || ',LOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_load_address || '''';
                      i_size := i_size + 1;
                      
                  end if;*/
                  /*if op_unload_address is null then
                      exec_sql := exec_sql || ',UNLOAD_ADDRESS';
                      exec_value := exec_value || ',''' || tmp_unload_address || '''';
                  end if;
                  if op_unload_contact is null then
                      exec_sql := exec_sql || ',UNLOAD_CONTACT';
                      exec_value := exec_value || ',''' || tmp_unload_contact || '''';
                  end if;
                  if op_unload_tel is null then
                      exec_sql := exec_sql || ',UNLOAD_TEL';
                      exec_value := exec_value || ',''' || tmp_unload_tel || '''';
                  end if;*/
              end if;
              --exec_value := exec_value || ',' || t_cur_value;
         end if;
         <<next_loop>>
             OUTPUT_RESULLT := '';
      end loop;
      select nvl(max(odr_row),0) + 1 into op_odr_row from trans_order_item where odr_no = op_odr_no;
      exec_sql := '';
      exec_value := '';
      for i in 1..i_key.count loop
          t_key := i_key(i);
          t_value := i_value(i);
          exec_sql := exec_sql || ',' || t_key;
          if t_value is null then 
              exec_value := exec_value || ',NULL';
          else
              exec_value := exec_value || ','''|| t_value || '''';
          end if;
      end loop;
      
      exec_sql := 'INSERT INTO TRANS_ORDER_ITEM(ODR_NO,ODR_ROW,ADDWHO,PACK_ID,ADDTIME' || exec_sql || ') VALUES(''' || op_odr_no || ''',' || op_odr_row || ',''' || IN_USERID || ''',''' || op_packid ||''',sysdate' || exec_value || ')';
      execute immediate exec_sql;

      ORDER_QTY_COUNT(op_odr_no, OUTPUT_RESULLT); --汇总订单信息
      --更新订单发货点和收货点信息
      UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select min(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select max(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;

      OUTPUT_RESULLT := '00';
  end loop;
  close C_ACT;
  commit;

  delete from TMP_ORDER_IMPORT WHERE TMPID = IN_TMPID;
  /*for h in 1..odr_lst.count loop
      op_odr_no := odr_lst(h);
      ORDER_QTY_COUNT(op_odr_no, OUTPUT_RESULLT); --汇总订单信息
      --更新订单发货点和收货点信息
      UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select min(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
      UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE  from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = (select max(odr_row) from trans_order_item where ODR_NO = op_odr_no)) where TRANS_ORDER_HEADER.ODR_NO = op_odr_no;
  end loop;*/
  OUTPUT_RESULLT := '00';
  commit;
  EXCEPTION
    WHEN OTHERS THEN
        --OUTPUT_RESULLT :='01'||sqlerrm || ']';
        OUTPUT_RESULLT := '01' || OUTPUT_RESULLT || '<br />数据第'|| r_LINE ||'行【'|| t_cur_name ||'】列值为[' || t_cur_value || '],数据异常请联系管理员!<br />';
        rollback;
        t_msg := substr(sqlerrm,1,200);
        insert into t_input_excel_temp(type_name,VALUE,line,column_name,column_cname,id,custom_odr_no)
        values(tmp_param_type,tmp_default_value,r_LINE,tmp_column,t_msg,to_char(sysdate,'YYYY-MM-DD HH24:MI'),tmp_table);

        delete from TMP_ORDER_IMPORT WHERE TMPID = IN_TMPID;
        for h in 1..odr_lst.count loop
            op_odr_no := odr_lst(h);
            delete from trans_order_header where odr_no = op_odr_no;
            delete from trans_order_item where odr_no = op_odr_no;
        end loop;
        commit;
        --RAISE_APPLICATION_ERROR(-20101, '执行全局校验时抛出异常: '||sqlerrm, TRUE);
END;

/

alter table T_INPUT_EXCEL_VERIFY add ERROR_MSG VARCHAR2(200) NULL;

--yuanlei t_input_excel_default
1	TRANS_ORDER_HEADER	ODR_TIME	select to_date('?','YYYY-MM-DD HH24:MI:SS') from dual	2	ODR_TIME	1
2	TRANS_ORDER_HEADER	CUSTOMER_NAME	select t.customer_cname from bas_customer t where t.CUSTOMER_CODE='?'	2	CUSTOMER_NAME	1
3	TRANS_ORDER_HEADER	REFENENCE1		3		1
4	TRANS_ORDER_HEADER	CUSTOM_ODR_NO		3		0
5	TRANS_ORDER_HEADER	BIZ_TYP	select id from bas_codes where name_c = '?' and prop_code = 'BIZ_TYP'	2	BIZ_TYP	1
6	TRANS_ORDER_HEADER	ODR_TYP	select id from bas_codes where name_c = '?' and prop_code = 'TRS_TYP'	2	ODR_TYP	1
7	TRANS_ORDER_HEADER	VEHICLE_TYP	select id from bas_vehicle_type where VEHICLE_TYPE = '?'	2	VEHICLE_TYP	1
8	TRANS_ORDER_HEADER	TEMPERATURE1		3		1
9	TRANS_ORDER_HEADER	TEMPERATURE2		3		1
10	TRANS_ORDER_ITEM	REQ_LOAD_TIME	select to_date('?','YYYY-MM-DD HH24:MI:SS') from dual	2	PRE_LOAD_TIME	1
11	TRANS_ORDER_ITEM	REQ_UNLOAD_TIME	select to_date('?','YYYY-MM-DD HH24:MI:SS') from dual	2	PRE_UNLOAD_TIME	1
12	TRANS_ORDER_HEADER	SLF_PICKUP_FLAG		3		1
13	TRANS_ORDER_HEADER	SLF_DELIVER_FLAG		3		1
14	TRANS_ORDER_HEADER	NOTES		3		1
15	TRANS_ORDER_ITEM	UNLOAD_CODE		3		1
16	TRANS_ORDER_ITEM	LOAD_CODE		3		1
17	TRANS_ORDER_ITEM	LOAD_ID	select t.id from bas_address t where t.addr_code = '?' and rownum = 1	4	LOAD_CODE	4
18	TRANS_ORDER_ITEM	UNLOAD_AREA_NAME		3		0
19	TRANS_ORDER_ITEM	UNLOAD_AREA_NAME2		3		0
20	TRANS_ORDER_ITEM	UNLOAD_AREA_NAME3		3		0
21	TRANS_ORDER_ITEM	LOAD_AREA_NAME		3		0
22	TRANS_ORDER_ITEM	LOAD_AREA_NAME2		3		0
23	TRANS_ORDER_ITEM	LOAD_AREA_NAME3		3		0
24	TRANS_ORDER_ITEM	UNLOAD_ADDRESS		3		0
25	TRANS_ORDER_ITEM	UNLOAD_CONTACT		3		0
26	TRANS_ORDER_ITEM	UNLOAD_TEL		3		0
27	TRANS_ORDER_ITEM	LOAD_ADDRESS		3		0
28	TRANS_ORDER_ITEM	LOAD_CONTACT		3		0
29	TRANS_ORDER_ITEM	LOAD_TEL		3		0
30	TRANS_ORDER_ITEM	SKU		3		2
31	TRANS_ORDER_ITEM	SKU_NAME		3		2
32	TRANS_ORDER_ITEM	QNTY		3		2
33	TRANS_ORDER_ITEM	QNTY_EACH	select '?' from dual	2	QNTY	2
34	TRANS_ORDER_ITEM	G_WGT		3		2
35	TRANS_ORDER_ITEM	VOL		3		2
36	TRANS_ORDER_ITEM	TEMPERATURE1	select id from bas_codes where prop_code = 'STOR_COND' and name_c = '?'	2	TEMPERATURE	2
37	TRANS_ORDER_ITEM	ID	select sys_guid() from dual	1		2
38	TRANS_ORDER_HEADER	TRANS_SRVC_ID	1	0		1
39	TRANS_ORDER_HEADER	TRANS_SRVC_ID_NAME	公路运输	0		1
40	TRANS_ORDER_HEADER	CREATE_ORG_ID	select F_GET_DEFAULT_ORGBYUSER('?', 0) from dual	2	USER_ID	1
41	TRANS_ORDER_HEADER	CREATE_ORG_ID_NAME	select F_GET_DEFAULT_ORGBYUSER('?', 1) from dual	2	USER_ID	1
42	TRANS_ORDER_HEADER	EXEC_ORG_ID	select F_GET_DEFAULT_ORGBYUSER('?', 0) from dual	2	USER_ID	1
43	TRANS_ORDER_HEADER	EXEC_ORG_ID_NAME	select F_GET_DEFAULT_ORGBYUSER('?', 1) from dual	2	USER_ID	1
44	TRANS_ORDER_HEADER	CUSTOMER_ID	select t.id from bas_customer t where t.CUSTOMER_CODE='?'	2	CUSTOMER_NAME	1
45	TRANS_ORDER_HEADER	TRANS_UOM	件	0		1
46	TRANS_ORDER_HEADER	STATUS	10	0		1
47	TRANS_ORDER_HEADER	EXEC_STAT	B41F05C6B85A4221813B833B103D2452	0		1
48	TRANS_ORDER_HEADER	DISCOUNT	1	0		1
49	TRANS_ORDER_ITEM	VOL_UNIT	M3	0		2
50	TRANS_ORDER_HEADER	ID	select sys_guid() from dual	1		1
51	TRANS_ORDER_ITEM	WGT_UNIT	T	0		2
52	TRANS_ORDER_ITEM	WORTH	0	0		2
53	TRANS_ORDER_ITEM	PLAN_STAT	10	0		2
54	TRANS_ORDER_ITEM	LOAD_STAT	10	0		2
55	TRANS_ORDER_ITEM	UNLOAD_STAT	10	0		2
56	TRANS_ORDER_ITEM	UOM		3		2
57	TRANS_ORDER_HEADER	STATUS_NAME	select name_c from bas_codes where code ='10' and prop_code='TRANS_ODR_STAT'	1		1
58	TRANS_ORDER_HEADER	BILL_FLAG	BILLY	0		1
59	TRANS_ORDER_ITEM	UNLOAD_ID	select t.id from bas_address t where t.addr_code = '?' and rownum = 1	5	UNLOAD_CODE	4
60	TRANS_ORDER_HEADER	BTCH_NUM		3		1

--t_input_excel_verify
1	CUSTOMER_NAME	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 100	内容超长
2	BIZ_TYP	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 100	内容超长
3	ODR_TYP	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 100	内容超长
4	BTCH_NUM	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
5	REFENENCE1	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
6	CUSTOM_ODR_NO	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
7	VEHICLE_TYP	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 32	内容超长
8	TEMPERATURE1	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select IS_NUMBER('?') from dual	非数值
9	TEMPERATURE1	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 5	内容超长
10	TEMPERATURE2	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select IS_NUMBER('?') from dual	非数值
11	TEMPERATURE2	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 5	内容超长
12	SLF_PICKUP_FLAG	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 1	内容超长
13	SLF_DELIVER_FLAG	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 1	内容超长
14	NOTES	4	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 255	内容超长
15	LOAD_CODE	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 100	内容超长
16	LOAD_AREA_NAME	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
17	LOAD_AREA_NAME2	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
18	LOAD_AREA_NAME3	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
19	LOAD_ADDRESS	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 300	内容超长
20	LOAD_CONTACT	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
21	LOAD_TEL	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
22	UNLOAD_CODE	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 100	内容超长
23	UNLOAD_AREA_NAME	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
24	UNLOAD_AREA_NAME2	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
25	UNLOAD_AREA_NAME3	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
26	UNLOAD_ADDRESS	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 300	内容超长
27	UNLOAD_CONTACT	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
28	UNLOAD_TEL	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
29	SKU	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 50	内容超长
30	SKU_NAME	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 200	内容超长
31	UOM	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 32	内容超长
32	QNTY	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 19	内容超长
33	VOL	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 19	内容超长
34	G_WGT	4	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where length('?')  <= 19	内容超长
35	LOAD_CODE	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_address where addr_code = '?'	无关联基础数据
36	UNLOAD_CODE	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_address where addr_code = '?'	无关联基础数据
37	QNTY	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where 0 <= '?'	不允许小于0
38	UNLOAD_AREA_NAME3	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_area t where t.area_cname = '?' and t.area_level = 5	无关联基础数据
39	LOAD_AREA_NAME3	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_area t where t.area_cname = '?' and t.area_level = 5	无关联基础数据
40	CUSTOMER_NAME	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from bas_customer where customer_code='?'	无关联基础数据
41	LOAD_AREA_NAME	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_area t where t.area_cname = '?' and t.area_level = 3	无关联基础数据
42	LOAD_AREA_NAME2	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_area t where t.area_cname = '?' and t.area_level = 4	无关联基础数据
43	UNLOAD_AREA_NAME	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_area t where t.area_cname = '?' and t.area_level = 3	无关联基础数据
44	UNLOAD_AREA_NAME2	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_area t where t.area_cname = '?' and t.area_level = 4	无关联基础数据
45	CUSTOMER_NAME	0	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT		不允许为空
46	BIZ_TYP	0	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT		不允许为空
47	LOAD_AREA_NAME	3	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	LOAD_CODE	无关联基础数据
48	LOAD_AREA_NAME2	3	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	LOAD_CODE	无关联基础数据
49	LOAD_ADDRESS	3	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	LOAD_CODE	无关联基础数据
50	LOAD_CONTACT	3	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	LOAD_CODE	无关联基础数据
51	LOAD_TEL	3	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	LOAD_CODE	无关联基础数据
52	UNLOAD_AREA_NAME	3	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	UNLOAD_CODE	无关联基础数据
53	UNLOAD_AREA_NAME2	3	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	UNLOAD_CODE	无关联基础数据
54	UNLOAD_ADDRESS	3	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	UNLOAD_CODE	无关联基础数据
55	SKU_NAME	0	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT		不允许为空
56	QNTY	0	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT		不允许为空
57	G_WGT	0	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT		不允许为空
58	VOL	0	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT		不允许为空
59	G_WGT	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select IS_NUMBER('?') from dual	非数值
60	VOL	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select IS_NUMBER('?') from dual	非数值
61	ODR_TYP	0	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT		不允许为空
62	SKU	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_sku where sku='?'	无关联基础数据
63	BIZ_TYP	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from bas_codes where prop_code = 'BIZ_TYP' and name_c = '?'	无关联基础数据
64	TEMPERATURE	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from bas_codes where prop_code = 'STOR_COND' and name_c = '?'	无关联基础数据
65	G_WGT	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where 0 <= '?'	不允许小于0
66	VOL	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select count(1) from dual where 0 <= '?'	不允许小于0
67	BTCH_NUM	1	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT		不唯一
68	ODR_TYP	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from bas_codes where prop_code = 'TRS_TYP' and name_c = '?'	无关联基础数据
69	VEHICLE_TYP	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select count(1) from bas_vehicle_type where VEHICLE_TYPE = '?' 	无关联基础数据
70	QNTY	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select IS_NUMBER('?') from dual	非数值
71	ODR_TIME	2	TRANS_ORDER_HEADER	TMP_ORDER_IMPORT	select IS_DATE('?') from dual	非日期
72	PRE_LOAD_TIME	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select IS_DATE('?') from dual	非日期
73	PRE_UNLOAD_TIME	2	TRANS_ORDER_ITEM	TMP_ORDER_IMPORT	select IS_DATE('?') from dual	非日期

--sys_function
2?	P02_B35	客户信息	客户信息	B	Y	P06_T09	0,P_F02,P_T019	wpsadmin	3	2093	2010/7/20	com.rd.client.view.base.BmsCustomerView	2081	wpsadmin	
--sys_func_page 2017-5-16
--lml
22972	P09_F022_P0_09	P09_F022_P0	生成对账单	150	Y	27-5月 -15	wpsadmin	生成对账单	B
22977	P02_B35_P0	P02_B35	按钮	150	Y	27-5月 -15	wpsadmin	按钮	B
22980	P02_B35_P0_03	P02_B35_P0	导出	150	Y	27-5月 -15	wpsadmin	导出	B
22979	P02_B35_P0_02	P02_B35_P0	取消	150	Y	27-5月 -15	wpsadmin	取消	B
22978	P02_B35_P0_01	P02_B35_P0	保存	150	Y	27-5月 -15	wpsadmin	保存	B

--yuanlei 2017-5-16
--bas_codes
6?	AA4F32C3168447D18782AEF6F241C72F	99	已关闭	已关闭	6	Y	N			2010/11/25 11:05:23	wpsadmin	TRANS_LOAD_STAT	wpsadmin	2010/11/25 11:37:48

create or replace procedure SP_LOADNO_DISPATCHAUDIT
(
   in_load_no LST,          --调度单号
   in_user_id varchar2,     --登录用户
   output_result out varchar2
   )
 IS
 --in_load_no LST := LST();

 t_load_no varchar2(50);

 t_plate_no varchar(50);
 t_driver varchar2(50);
 t_mobile varchar2(50);
 t_suplr_id varchar(32);
 t_suplr_name varchar2(50);
 t_veh_sign varchar2(50);
 t_exec_org_id varchar2(32);

 op_license varchar2(50);
 op_load_count number(4);
 op_unload_count number(4);
 sys_count number;
 t_min_time date;
 
 t_temp_no1 varchar2(100);
 t_temp_no2 varchar2(100);
 t_gps_no1 varchar2(100);
 
 

CURSOR shpm IS
      SELECT ODR_NO FROM TRANS_SHIPMENT_HEADER WHERE load_no = t_load_no;

 BEGIN
      output_result:='00';--执行成功
      --in_load_no.extend;
      --in_load_no(1) := '150105115268';
      --dbms_output.put_line('start:' || to_char(sysdate,'yyyy-mm-dd hh:mi:ss'));
      for i in 1..in_load_no.count loop

          t_load_no := in_load_no(i);
          select plate_no,driver1,mobile1,suplr_id,suplr_name,exec_org_id,veh_sign,TEMP_NO1,TEMP_NO2,GPS_NO1  
          into t_plate_no,t_driver,t_mobile,t_suplr_id,t_suplr_name,t_exec_org_id,t_veh_sign,t_temp_no1,t_temp_no2,t_gps_no1 
          from trans_load_header where load_no = t_load_no;
          --select distinct load_name,unload_name into op_load_name,op_unload_name from trans_shipment_header where load_no = t_load_no;
          select count(distinct load_id) into op_load_count from trans_shipment_header where load_no = t_load_no;
          select count(distinct unload_id) into op_unload_count from trans_shipment_header where load_no = t_load_no;
          /*if t_temp_no1 is null and t_temp_no2 is null and t_gps_no1 is null then
              output_result := '01调度单[' || in_load_no(i) || ']必须填写温控设备或GPS!';
              return; 
          end if;*/
          if t_plate_no is null then
              output_result := '01调度单[' || in_load_no(i) || ']车牌号为空,不能执行配车确认!';
              return;
          end if;
          if t_suplr_id is null then
              output_result := '01调度单[' || in_load_no(i) || ']供应商为空,不能执行配车确认!';
              return;
          end if;
          --如果调度单下的作业单车牌号为空，把调度单的车牌号带入其作业单中
          --select count(1) into sys_count from trans_shipment_header where load_no = in_load_no(i) and plate_no is null;
          --if sys_count>0 then
             update trans_shipment_header set plate_no = t_plate_no,driver=t_driver,mobile=t_mobile where load_no = t_load_no;
             --update trans_load_job set plate_no = t_plate_no where load_no = in_load_no(i) and load_status = '10' ;
          --end if;
          --UPDATE TRANS_LOAD_HEADER SET dispatch_stat='9EC53E33BDEB4806AAC8CD2904BFD1BC' WHERE load_no = t_load_no;
          --2014-3-4 配车审核后重算调度单数量、体积
          UPDATE TRANS_LOAD_HEADER SET (TOT_GROSS_W, TOT_NET_W, TOT_VOL, TOT_WORTH, TOT_QNTY, TOT_QNTY_EACH,dispatch_stat,dispatch_stat_name,LOAD_COUNT,UNLOAD_COUNT)
          = ( select SUM(TOT_GROSS_W) as SUM_GROSS_W,SUM(TOT_NET_W) AS SUM_NET_W
            ,SUM(TOT_VOL) AS SUM_VOL,SUM(TOT_WORTH) AS SUM_WORTH,SUM(TOT_QNTY) AS SUM_QNTY,SUM(TOT_QNTY_EACH) AS SUM_QNTY_EACH,'9EC53E33BDEB4806AAC8CD2904BFD1BC','已审核',op_load_count,op_unload_count
            from TRANS_SHIPMENT_HEADER where TRANS_SHIPMENT_HEADER.LOAD_NO = TRANS_LOAD_HEADER.LOAD_NO
            group by LOAD_NO
            ),PRE_DEPART_TIME = DEPART_TIME where LOAD_NO = t_load_no;

          --插入业务日志
          insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
             values(sys_guid(),RDPG_STAT.DISPATCH_AUDIT,RDPG_STAT.LOAD_NO,t_load_no,'调度单配车审核',sysdate,in_user_id);
          --插入作业单客户日志
          --SP_CUSTOMACT_LOG(RDPG_STAT.DISPATCH_AUDIT,RDPG_STAT.LOAD_NO,t_load_no,in_user_id,sysdate , output_result);
          SP_SFSTATUS_LOG('','',t_load_no,'','030',in_user_id);

          --生成手机APP使用的工号
          select count(1) into sys_count from bas_staff t where staff_name = t_driver and staff_typ = '4D279652C2B9423D8AD958E63B9B3547';-- and org_id = t_exec_org_id;
          if sys_count = 1 then
              select t.oper_license into op_license from bas_staff t where staff_name = t_driver and staff_typ = '4D279652C2B9423D8AD958E63B9B3547';-- and org_id = t_exec_org_id;
              if op_license is null then
                  op_license := 'wpsadmin';
              end if;
              update trans_load_header set drvr_lic_num = op_license where load_no = t_load_no;
          else
              update trans_load_header set drvr_lic_num = 'wpsadmin' where load_no = t_load_no;
          end if;

          begin
              sp_time_calculate('','LOAD_NO','SHPM_NO',t_load_no,1,2,output_result);
              output_result := '00';
              for it in shpm loop
                  SELECT min(leave_whse_time) into t_min_time from TRANS_SHIPMENT_HEADER WHERE odr_no = it.odr_no;
                  --更新原始托运单状态
                  UPDATE TRANS_ORDER_HEADER SET PRE_WHSE_TIME = t_min_time WHERE odr_no = it.odr_no;
              end loop;
          exception when OTHERS THEN
              t_min_time := null;
          end;
         commit;
      end loop;
      --dbms_output.put_line('end:' || to_char(sysdate,'yyyy-mm-dd hh:mi:ss'));
      EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result:='01'||sqlerrm;
      return;
  END;
/

CREATE OR REPLACE PROCEDURE SP_LOADNO_SENDCONFIRM(
   in_load_no varchar2,          --调度单号
   in_load_time varchar2,        --发运时间
   in_shpm_no LST,               --作业单号
   in_user_id varchar2,          --登录用户
   output_result out varchar2
   )
  /**
   * 版本V2.1
   */
 IS
  t_count number(8);
  t_load_count number(8);
  t_sended_count number(8);
  t_load_status varchar(32);
  t_odr_no varchar(50);
  t_shpm_no varchar(100);
  t_biz_typ varchar2(32);
  t_trans_srvc_id varchar2(32);

  t_customer_id varchar(32);
  t_max_time date;
  t_min_time date;
  t_split_flag varchar(1);
  t_depart_time varchar2(30);
  t_dispatch_stat varchar2(32);
  op_sum_qnty number(18,8);
  op_sum_vol number(18,8);
  op_sum_gwgt number(18,8);
  op_sum_nwgt number(18,8);
  op_sum_worth number(18,8);
  op_exec_org_id varchar(32);
  op_suplr_id varchar(32);
  op_suplr_name varchar(50);
  op_vehicle_typ_id varchar(32);
  op_plate_no varchar(20);
  op_driver varchar(20);
  op_mobile varchar(50);
  op_notes varchar(2048);
  op_username varchar(50);
  op_status varchar(50);
  op_done_time date;
  op_start_area varchar2(100);
  op_end_area varchar2(100);
  op_tempeq1 varchar2(50);
  op_tempeq2 varchar2(50);
  op_gps_no varchar2(50);
  op_equip1 varchar2(50);
  op_equip2 varchar2(50);
  op_equip3 varchar2(50);
  op_load_time varchar2(30);
  --op_load_time date;
  --in_shpm_no LST := LST();

  --yuanlei 2013-06-04
  t_sendable_flag char(1);
  t_msg varchar2(100);
  v_output_result varchar(50);
  --yuanlei
  CURSOR header  IS
        SELECT SHPM_NO,PARN_SHPM_NO,ODR_NO,SPLIT_FLAG,customer_id,CUSTOM_ODR_NO,LOAD_NAME,STATUS,LOAD_AREA_NAME,ODR_TIME,ASSIGN_TIME
        ,PRE_UNLOAD_TIME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,EXEC_ORG_ID,FIRST_JRNY_NO,LAST_JRNY_NO,BIZ_TYP,TRANS_SRVC_ID,DEPART_TIME
          FROM TRANS_SHIPMENT_HEADER WHERE  load_no =in_load_no;

   CURSOR item IS
                   SELECT ODR_ROW FROM TRANS_ORDER_ITEM WHERE ODR_NO = t_odr_no;
  --CURSOR item;
   BEGIN
      output_result:='00';--执行成功
     -- in_shpm_no.extend;
      --in_shpm_no(1) := '201108160344_S1';
      /*if length(in_load_time) > 19 then
          op_load_time := substr(in_load_time,0,18);
      else
          op_load_time := in_load_time;
      end if;*/
      op_load_time := to_char(sysdate,'YYYY-MM-DD HH24:MI:SS');
      select exec_org_id,suplr_id,suplr_name,plate_no,vehicle_typ_id,driver1,mobile1,status,dispatch_stat,done_time,start_area_name,end_area_name,temp_no1,temp_no2,gps_no1
          into op_exec_org_id,op_suplr_id,op_suplr_name,op_plate_no,op_vehicle_typ_id,op_driver,op_mobile,op_status,t_dispatch_stat,op_done_time,op_start_area,op_end_area,op_tempeq1,op_tempeq2,op_gps_no
          from TRANS_LOAD_HEADER WHERE load_no = in_load_no;
      if t_dispatch_stat != '9EC53E33BDEB4806AAC8CD2904BFD1BC' then
         output_result := '01未审核的调度单不能发运!';
         return;
      end if;
      if op_tempeq1 is null and op_tempeq2 is null and op_gps_no is null then
              output_result := '01调度单[' || in_load_no || ']必须填写温控设备或GPS!';
              return; 
          end if;
      --2013-06-04 yuanlei 作业单发运校验（针对行程拆分）
      if trim(in_shpm_no(1)) is null then
          FOR h IN header LOOP
              t_shpm_no := h.shpm_no;
              if INSTR(t_shpm_no,'_M') > 0 THEN
                  select sendable_flag into t_sendable_flag from trans_shipment_header where shpm_no = t_shpm_no;
                  if t_sendable_flag = 'N' then
                      output_result := '01作业单[' || t_shpm_no || ']上一段未签收，不允许发运!';
                      return;
                  end if;
              END IF;
          end loop;
      else
          for i in 1..in_shpm_no.count loop
              t_shpm_no := in_shpm_no(i);
              if INSTR(t_shpm_no,'_M') > 0 THEN
                  select sendable_flag into t_sendable_flag from trans_shipment_header where shpm_no = t_shpm_no;
                  if t_sendable_flag = 'N' then
                      output_result := '01作业单[' || t_shpm_no || ']上一段未签收，不允许发运!';
                      return;
                  end if;
              END IF;
          end loop;
      end if;
      --yuanlei

     /* select count(1) into t_count from sys_user where user_id = in_user_id;
      if t_count = 0 then
          op_username := in_user_id;
      else
          select user_name into op_username from sys_user where user_id = in_user_id;
      end if;*/
      if trim(in_shpm_no(1)) is null then
          --针对所有作业单做发运确认

          --更新调度单状态为完全发货
          UPDATE TRANS_LOAD_HEADER SET status=RDPG_STAT.TRANS_DEPART,status_name=RDPG_STAT.TRANS_DEPART_NAME,PRE_DEPART_TIME = to_date(op_load_time,'yyyy-MM-dd HH24:mi:ss'),DEPART_TIME = to_date(op_load_time,'yyyy-MM-dd HH24:mi:ss'), edittime = sysdate,editwho = in_user_id
             WHERE load_no =in_load_no;

          FOR h IN header LOOP
              --更新作业单状态和发运时间
              t_odr_no := h.odr_no;
              t_shpm_no := h.shpm_no;
              t_customer_id := h.customer_id;
              t_biz_typ := h.biz_typ;
              t_trans_srvc_id := h.trans_srvc_id;
              if h.depart_time is not null then
                  t_depart_time := to_char(h.depart_time,'YYYY-MM-DD HH24:MI:SS');
              else
                  t_depart_time := op_load_time;
              end if;
              UPDATE TRANS_SHIPMENT_HEADER SET status=RDPG_STAT.SHPM_LOAD,status_name=RDPG_STAT.SHPM_LOAD_NAME,DEPART_TIME = to_date(t_depart_time,'yyyy-MM-dd HH24:mi:ss'),OP_LOAD_TIME = sysdate,PLATE_NO = op_plate_no
                , VEHICLE_TYP_ID = op_vehicle_typ_id, DRIVER = op_driver, MOBILE = op_mobile,suplr_id = op_suplr_id,suplr_name = op_suplr_name--,PRE_WHSE_TIME = op_done_time
                WHERE shpm_no = t_shpm_no;
              --更新所有作业明细的发货数量、体积、毛重、净重、货值等值
              UPDATE TRANS_SHIPMENT_ITEM SET ld_qnty=qnty,ld_vol=vol,ld_gwgt=g_wgt,ld_nwgt=n_wgt,ld_worth=worth WHERE shpm_no = t_shpm_no;

              --自动运算时间
              begin
                  sp_time_calculate('','SHPM_NO','SHPM_NO',t_shpm_no,2,3,output_result);
              end;

              IF h.split_flag = 'N' THEN   --当前作业单为原始作业单
                  --SELECT MAX(pre_whse_time) into t_min_time from TRANS_SHIPMENT_HEADER WHERE odr_no = t_odr_no;
                  --更新该作业单对应的原始托运单状态
                  UPDATE TRANS_ORDER_HEADER SET LOAD_STAT = RDPG_STAT.LOADED,LOAD_TIME = to_date(op_load_time,'yyyy-MM-dd HH24:mi:ss') WHERE odr_no = t_odr_no;
                  --插入托运单客户日志
                  SP_CUSTOMACT_LOG(RDPG_STAT.SEND,RDPG_STAT.ODR_NO,t_odr_no,in_user_id,to_date(op_load_time,'yyyy-MM-dd HH24:mi:ss') , output_result);
              ELSE                                 --当前作业单为子作业单或按发货仓库生成的作业单
                  SELECT COUNT(*) INTO t_count FROM TRANS_SHIPMENT_HEADER
                    WHERE odr_no = t_odr_no and SPLIT_FLAG = 'Y' AND SHPM_NO != t_shpm_no AND status<RDPG_STAT.SHPM_LOAD;
                  IF t_count = 0 THEN    --全部发运
                      t_load_status := RDPG_STAT.LOADED;
                      --插入托运单客户日志
                      SP_CUSTOMACT_LOG(RDPG_STAT.SEND,RDPG_STAT.ODR_NO,t_odr_no,in_user_id,t_max_time, output_result);
                  ELSE                  --未全部发运
                      t_load_status := RDPG_STAT.PART_LOAD;
                  END IF;

                  SELECT min(depart_time) into t_max_time from TRANS_SHIPMENT_HEADER WHERE odr_no = t_shpm_no;

                  --更新原始托运单状态、实际发运时间
                  UPDATE TRANS_ORDER_HEADER SET LOAD_STAT=t_load_status,LOAD_TIME = t_max_time WHERE odr_no = t_odr_no;

               END IF;
               for it in item loop
                   SELECT SUM(nvl(LD_QNTY,0)),SUM(nvl(LD_VOL,0)),SUM(nvl(LD_GWGT,0)),SUM(nvl(LD_NWGT,0)),SUM(nvl(LD_WORTH,0)) INTO op_sum_qnty,op_sum_vol,op_sum_gwgt,op_sum_nwgt,op_sum_worth
                   FROM trans_shipment_item WHERE odr_no=t_odr_no and shpm_row =it.odr_row;

                   UPDATE TRANS_ORDER_ITEM SET  LD_QNTY=op_sum_qnty,LD_VOL=op_sum_vol,LD_GWGT=op_sum_gwgt,LD_NWGT=op_sum_nwgt,LD_WORTH=op_sum_worth
                   WHERE odr_no=t_odr_no and odr_row = it.odr_row;
               end loop;
               --插入业务日志
               insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
                 values(sys_guid(),RDPG_STAT.SEND,RDPG_STAT.SHPM_NO,t_shpm_no,'调度单' || in_load_no ||  '发货确认',sysdate,in_user_id);
               --插入作业单客户日志
               --SP_SFSTATUS_LOG(t_biz_typ,t_trans_srvc_id,in_load_no,t_shpm_no,RDPG_STAT.SHPM_LOAD,in_user_id);
               --SP_CUSTOMACT_LOG(RDPG_STAT.SEND,RDPG_STAT.SHPM_NO,t_shpm_no,in_user_id,to_date(in_load_time,'yyyy-MM-dd HH24:mi:ss') , output_result);
               --SP_FEE_CALCULATE(t_shpm_no, 'LOAD', in_user_id, output_result);
           END LOOP;

           --自动给每个作业单写笔跟踪记录
           /*INSERT INTO TRANS_TRACK_TRACE(ID,LOAD_NO,SHPM_NO,ODR_NO,PLATE_NO,DRIVER,MOBILE,TRACER,EXEC_ORG_ID,TRACE_TIME,ABNOMAL_STAT,ABNOMAL_NOTE,
             PRE_SOLVE_TIME,SOLUTION,SOLVE_TIME,PRE_UNLOAD_TIME,CURRENT_LOC,LONGITUDE,LATITUDE,SPEED,TEMPERATURE,SHOW_SEQ,INFORMATION,ADDWHO,ADDTIME)
           select sys_guid(),LOAD_NO,SHPM_NO,ODR_NO,op_plate_no,op_driver,op_mobile,op_username,op_exec_org_id,sysdate,'5FB42E7D159346C395A2A34E0FE698C1','',
             null,null,null,PRE_UNLOAD_TIME,LOAD_AREA_NAME,'0','0','0','',null,'货物发出',in_user_id,sysdate
           from TRANS_SHIPMENT_HEADER where load_no = in_load_no;*/

           --插入业务日志
           insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
             values(sys_guid(),RDPG_STAT.SEND,RDPG_STAT.LOAD_NO,in_load_no,'调度单' || in_load_no ||  '发货确认',sysdate,in_user_id);
           --自动计算运费
           --delete from TRANS_BILL_PAY WHERE LOAD_NO = in_load_no;
           --SP_PAY_CALCUALTE(in_load_no,t_shpm_no,in_user_id,'Y',t_msg);

           /*insert into bms_load_header(ID,LOAD_NO,STATUS,EXEC_ORG_ID,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER1,MOBILE1,
           DRVR_LIC_NUM,CONFIRMOR,CONFIRM_TIME,REF_NO,START_AREA_ID,START_AREA_NAME,END_AREA_ID,END_AREA_NAME,
           TRANS_SRVC_ID,START_ODOMETER,RETURN_ODOMETER,MILEAGE,EMPTY_MILEAGE,LADEN_MILEAGE,TRAILER_NO,DRIVER2,
           MOBILE2,PRE_PICKUP_TIME,PRE_DEPART_TIME,PICKUP_TIME,DEPART_TIME,ROUTE_ID,AP_TARRIF_ID,AUDIT_STAT,AUDIT_TIME,
           AUDITOR,PAY_STAT,PRE_PAY_TIME,PAY_TIME,PAYER,PRINT_FLAG,PRINT_TMS,ABNOMAL_STAT,SEAL_NO,TOT_QNTY,TOT_VOL,
           TOT_GROSS_W,TOT_WORTH,TRANS_UOM,CURRENT_ORG_ID,CURRENT_LOC,NOTES,ADDWHO,ADDTIME,EDITWHO,EDITTIME,
           AUDITED_FLAG,DONE_TIME,TOT_NET_W,ABNOMAL_NOTES,DISPATCH_STAT,SETT_MILE,TOT_QNTY_EACH,UDF1,UDF2,UDF3,UDF4,
           EXEC_ORG_ID_NAME,STATUS_NAME,DISPATCH_STAT_NAME,REMAIN_GROSS_W,REMAIN_VOL,MAX_GROSS_W,MAX_VOL,TEMPERATURE1,
           TEMPERATURE2,VEH_SIGN,LOAD_STAT,SIGN_ORG_ID,ACCOUNT_STAT,FEEAUDIT_STAT,ACCOUNT_TIME,FEEAUDIT_TIME,CHECK_FLAG,
           OP_PICKUP_TIME,GPS_NO1,GPS_NO2,TEMP_NO1,TEMP_NO2,LOAD_COUNT,UNLOAD_COUNT,PRE_UNLOAD_TIME,ARRIVE_WHSE_TIME,
           START_LOAD_TIME,END_LOAD_TIME,QUALIFIED_FLAG,AUDIT_NOTES,TOT_AMOUNT)
              
           select ID,LOAD_NO,STATUS,EXEC_ORG_ID,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER1,MOBILE1,
           DRVR_LIC_NUM,CONFIRMOR,CONFIRM_TIME,REF_NO,START_AREA_ID,START_AREA_NAME,END_AREA_ID,END_AREA_NAME,
           TRANS_SRVC_ID,START_ODOMETER,RETURN_ODOMETER,MILEAGE,EMPTY_MILEAGE,LADEN_MILEAGE,TRAILER_NO,DRIVER2,
           MOBILE2,PRE_PICKUP_TIME,PRE_DEPART_TIME,PICKUP_TIME,DEPART_TIME,ROUTE_ID,AP_TARRIF_ID,AUDIT_STAT,AUDIT_TIME,
           AUDITOR,PAY_STAT,PRE_PAY_TIME,PAY_TIME,PAYER,PRINT_FLAG,PRINT_TMS,ABNOMAL_STAT,SEAL_NO,TOT_QNTY,TOT_VOL,
           TOT_GROSS_W,TOT_WORTH,TRANS_UOM,CURRENT_ORG_ID,CURRENT_LOC,NOTES,ADDWHO,ADDTIME,EDITWHO,EDITTIME,
           AUDITED_FLAG,DONE_TIME,TOT_NET_W,ABNOMAL_NOTES,DISPATCH_STAT,SETT_MILE,TOT_QNTY_EACH,UDF1,UDF2,UDF3,UDF4,
           EXEC_ORG_ID_NAME,STATUS_NAME,DISPATCH_STAT_NAME,REMAIN_GROSS_W,REMAIN_VOL,MAX_GROSS_W,MAX_VOL,TEMPERATURE1,
           TEMPERATURE2,VEH_SIGN,LOAD_STAT,SIGN_ORG_ID,ACCOUNT_STAT,FEEAUDIT_STAT,ACCOUNT_TIME,FEEAUDIT_TIME,CHECK_FLAG,
           OP_PICKUP_TIME,GPS_NO1,GPS_NO2,TEMP_NO1,TEMP_NO2,LOAD_COUNT,UNLOAD_COUNT,PRE_UNLOAD_TIME,ARRIVE_WHSE_TIME,
           START_LOAD_TIME,END_LOAD_TIME,QUALIFIED_FLAG,AUDIT_NOTES,TOT_AMOUNT from trans_load_header where load_no = in_load_no;
              
           insert into BMS_SHIPMENT_HEADER(ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,ODR_TIME,
           LOAD_NO,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,
           PRE_LOAD_TIME,DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,
           UNLOAD_TEL,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,STATUS,BILL_TO,REFENENCE1,REFENENCE2,
           REFENENCE3,REFENENCE4,CREATE_ORG_ID,EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,COD_FLAG,TOT_QNTY,
           TRANS_UOM,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,
           SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,SPLIT_FLAG,
           SPLIT_REASON_CODE,SPLIT_REASON,ADDTIME,ADDWHO,EDITTIME,EDITWHO,ASSIGN_STAT,ABNOMAL_NOTES,LOSDAM_FLAG,
           START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,OP_LOAD_TIME,SIGNATARY,OP_UNLOAD_TIME,
           OP_POD_TIME,LOAD_SEQ,UNLOAD_SEQ,LOAD_DELAY_REASON,UNLOAD_DELAY_REASON,POD_DELAY_REASON,CURRENT_LOC,WHSE_ID,
           UNLOAD_DELAY_DAYS,POD_DELAY_DAYS,SATISFY_CODE,SERVICE_CODE,LOAD_PRINT_COUNT,SHPM_PRINT_COUNT,PRE_WHSE_TIME,
           PRE_ASSIGN_TIME,ASSIGN_TIME,PRE_END_LOAD_TIME,FACT_MILE,SETT_MILE,ROUTE_MILE,PICKING_STAT,TRACK_NOTES,
           LEAVE_WHSE_TIME,ARRIVE_WHSE_TIME,BELONG_JRNY_NO,FIRST_JRNY_NO,LAST_JRNY_NO,SENDABLE_FLAG,END_UNLOAD_ID,
           END_UNLOAD_NAME,END_UNLOAD_ADDRESS,RECE_FLAG,UNION_SHPM_NO,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,
           STATUS_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,
           UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,END_TEL,END_CONTACT,
           SIGN_ORG_ID_NAME,BIZ_TYP,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,LOAD_REGION,
           UNLOAD_REGION,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,
           TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME)
           select ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,ODR_TIME,
           LOAD_NO,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,
           PRE_LOAD_TIME,DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,
           UNLOAD_TEL,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,STATUS,BILL_TO,REFENENCE1,REFENENCE2,
           REFENENCE3,REFENENCE4,CREATE_ORG_ID,EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,COD_FLAG,TOT_QNTY,
           TRANS_UOM,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,
           SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,SPLIT_FLAG,
           SPLIT_REASON_CODE,SPLIT_REASON,ADDTIME,ADDWHO,EDITTIME,EDITWHO,ASSIGN_STAT,ABNOMAL_NOTES,LOSDAM_FLAG,
           START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,OP_LOAD_TIME,SIGNATARY,OP_UNLOAD_TIME,
           OP_POD_TIME,LOAD_SEQ,UNLOAD_SEQ,LOAD_DELAY_REASON,UNLOAD_DELAY_REASON,POD_DELAY_REASON,CURRENT_LOC,WHSE_ID,
           UNLOAD_DELAY_DAYS,POD_DELAY_DAYS,SATISFY_CODE,SERVICE_CODE,LOAD_PRINT_COUNT,SHPM_PRINT_COUNT,PRE_WHSE_TIME,
           PRE_ASSIGN_TIME,ASSIGN_TIME,PRE_END_LOAD_TIME,FACT_MILE,SETT_MILE,ROUTE_MILE,PICKING_STAT,TRACK_NOTES,
           LEAVE_WHSE_TIME,ARRIVE_WHSE_TIME,BELONG_JRNY_NO,FIRST_JRNY_NO,LAST_JRNY_NO,SENDABLE_FLAG,END_UNLOAD_ID,
           END_UNLOAD_NAME,END_UNLOAD_ADDRESS,RECE_FLAG,UNION_SHPM_NO,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,
           STATUS_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,
           UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,END_TEL,END_CONTACT,
           SIGN_ORG_ID_NAME,BIZ_TYP,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,LOAD_REGION,
           UNLOAD_REGION,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,
           TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME
           from TRANS_SHIPMENT_HEADER where load_no = in_load_no;*/
           --insert into tmp_order_header select * from trans_order_header where odr_no = t_odr_no;
           --insert into tmp_order_item select * from trans_order_item where odr_no = t_odr_no;

           output_result:='00调度单[' || in_load_no || ']发运成功';--执行成功
      else
          --针对选定的作业单进行发运
          select count(1) into t_load_count from TRANS_SHIPMENT_HEADER WHERE load_no = in_load_no;
          --select max(status) into t_load_count from trans_shipment_header where load_no=in_load_no;
          select count(1) into t_sended_count from TRANS_SHIPMENT_HEADER WHERE status >= RDPG_STAT.SHPM_LOAD and status <= RDPG_STAT.SHPM_CLOSED and load_no = in_load_no;
          for i in 1..in_shpm_no.count loop
              t_shpm_no := in_shpm_no(i);
              op_notes := op_notes || t_shpm_no|| ',';
              select odr_no,split_flag,biz_typ,trans_srvc_id into t_odr_no,t_split_flag,t_biz_typ,t_trans_srvc_id from TRANS_SHIPMENT_HEADER WHERE shpm_no = t_shpm_no;

              --更新作业单状态和发运时间
              UPDATE TRANS_SHIPMENT_HEADER SET status=RDPG_STAT.SHPM_LOAD,status_name=RDPG_STAT.SHPM_LOAD_NAME,DEPART_TIME = to_date(op_load_time,'yyyy-MM-dd HH24:mi:ss'),OP_LOAD_TIME = sysdate
                 , VEHICLE_TYP_ID = op_vehicle_typ_id, DRIVER = op_driver, MOBILE = op_mobile,suplr_id = op_suplr_id,suplr_name = op_suplr_name
                WHERE shpm_no = t_shpm_no;
              --更新所有作业明细的发货数量、体积、毛重、净重、货值等值
              UPDATE TRANS_SHIPMENT_ITEM SET ld_qnty=qnty,ld_vol=vol,ld_gwgt=g_wgt,ld_nwgt=n_wgt,ld_worth=worth WHERE shpm_no = t_shpm_no;

               --自动运算时间
               begin
                   sp_time_calculate('','SHPM_NO','SHPM_NO',t_shpm_no,2,3,output_result);
               end;

              IF t_split_flag = 'N' THEN   --当前作业单为原始作业单
                  SELECT min(pre_whse_time) into t_min_time from TRANS_SHIPMENT_HEADER WHERE odr_no = t_odr_no;
                  --更新该作业单对应的原始托运单状态
                  UPDATE TRANS_ORDER_HEADER SET LOAD_STAT = RDPG_STAT.LOADED,LOAD_TIME = to_date(op_load_time,'yyyy-MM-dd HH24:mi:ss'),pre_whse_time = t_min_time WHERE odr_no = t_odr_no;
                  --插入托运单客户日志
                  SP_CUSTOMACT_LOG(RDPG_STAT.SEND,RDPG_STAT.ODR_NO,t_odr_no,in_user_id,to_date(op_load_time,'yyyy-MM-dd HH24:mi:ss') , output_result);
              ELSE                                 --当前作业单为子作业单或按发货仓库生成的作业单
                  SELECT COUNT(*) INTO t_count FROM TRANS_SHIPMENT_HEADER
                    WHERE odr_no = t_odr_no and SPLIT_FLAG = 'Y' AND SHPM_NO != t_shpm_no AND status<RDPG_STAT.SHPM_LOAD;
                  IF t_count = 0 THEN    --全部发运
                      t_load_status := RDPG_STAT.LOADED;
                      --插入托运单客户日志
                      SP_CUSTOMACT_LOG(RDPG_STAT.SEND,RDPG_STAT.ODR_NO,t_odr_no,in_user_id,t_max_time, output_result);
                  ELSE                  --未全部发运
                      t_load_status := RDPG_STAT.PART_LOAD;
                  END IF;

                  SELECT Min(depart_time),min(pre_whse_time) into t_max_time,t_min_time from TRANS_SHIPMENT_HEADER WHERE odr_no = t_odr_no;

                  --更新原始托运单状态、实际发运时间
                  UPDATE TRANS_ORDER_HEADER SET LOAD_STAT=t_load_status,LOAD_TIME = t_max_time,pre_whse_time = t_min_time WHERE odr_no = t_odr_no;

               END IF;
               for it in item loop
                   SELECT SUM(nvl(LD_QNTY,0)),SUM(nvl(LD_VOL,0)),SUM(nvl(LD_GWGT,0)),SUM(nvl(LD_NWGT,0)),SUM(nvl(LD_WORTH,0)) INTO op_sum_qnty,op_sum_vol,op_sum_gwgt,op_sum_nwgt,op_sum_worth
                   FROM trans_shipment_item WHERE odr_no=t_odr_no and shpm_row =it.odr_row;

                   UPDATE TRANS_ORDER_ITEM SET  LD_QNTY=op_sum_qnty,LD_VOL=op_sum_vol,LD_GWGT=op_sum_gwgt,LD_NWGT=op_sum_nwgt,LD_WORTH=op_sum_worth
                   WHERE odr_no=t_odr_no and odr_row = it.odr_row;
               end loop;
               --插入业务日志
               insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
                 values(sys_guid(),RDPG_STAT.SEND,RDPG_STAT.SHPM_NO,t_shpm_no,'作业单发货确认',sysdate,in_user_id);
               --插入作业单客户日志
               SP_CUSTOMACT_LOG(RDPG_STAT.SEND,RDPG_STAT.SHPM_NO,t_shpm_no,in_user_id,to_date(op_load_time,'yyyy-MM-dd HH24:mi:ss') , output_result);
               SP_SFSTATUS_LOG(t_biz_typ,t_trans_srvc_id,in_load_no,t_shpm_no,RDPG_STAT.SHPM_LOAD,in_user_id);
          end loop;
          op_notes := substr(op_notes, 1, length(op_notes) - 1);
          --fanglm 2012-02-21
          if (in_shpm_no.count + t_sended_count) = t_load_count then
            --if t_load_count >= 40 then
              --更新调度单状态为完全发货
              select max(depart_time) into t_max_time from trans_shipment_header where load_no = in_load_no;
              UPDATE TRANS_LOAD_HEADER SET status=RDPG_STAT.TRANS_DEPART,status_name=RDPG_STAT.TRANS_DEPART_NAME,DEPART_TIME = t_max_time, edittime = sysdate,editwho = in_user_id
                WHERE load_no =in_load_no;

               for h in header loop
                   sp_time_calculate(h.customer_id,'SHPM_NO','SHPM_NO',h.shpm_no,4,6,output_result);
               end loop;

              /*--自动给每个作业单写笔跟踪记录
              INSERT INTO TRANS_TRACK_TRACE(ID,LOAD_NO,SHPM_NO,ODR_NO,PLATE_NO,DRIVER,MOBILE,TRACER,EXEC_ORG_ID,TRACE_TIME,ABNOMAL_STAT,ABNOMAL_NOTE,
                 PRE_SOLVE_TIME,SOLUTION,SOLVE_TIME,PRE_UNLOAD_TIME,CURRENT_LOC,LONGITUDE,LATITUDE,SPEED,TEMPERATURE,SHOW_SEQ,INFORMATION,ADDWHO,ADDTIME)
              select sys_guid(),LOAD_NO,SHPM_NO,ODR_NO,op_plate_no,op_driver,op_mobile,op_username,op_exec_org_id,sysdate,'5FB42E7D159346C395A2A34E0FE698C1','',
                 null,null,null,PRE_UNLOAD_TIME,LOAD_AREA_NAME,'0','0','0','',null,'货物发出',in_user_id,sysdate
               from TRANS_SHIPMENT_HEADER where load_no = in_load_no and status = '40';*/

              --自动计算运费
              --delete from TRANS_BILL_PAY WHERE LOAD_NO = in_load_no;
              --SP_PAY_CALCUALTE(in_load_no,in_shpm_no(1),in_user_id,'Y',t_msg);
              /*insert into bms_load_header(ID,LOAD_NO,STATUS,EXEC_ORG_ID,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER1,MOBILE1,
              DRVR_LIC_NUM,CONFIRMOR,CONFIRM_TIME,REF_NO,START_AREA_ID,START_AREA_NAME,END_AREA_ID,END_AREA_NAME,
              TRANS_SRVC_ID,START_ODOMETER,RETURN_ODOMETER,MILEAGE,EMPTY_MILEAGE,LADEN_MILEAGE,TRAILER_NO,DRIVER2,
              MOBILE2,PRE_PICKUP_TIME,PRE_DEPART_TIME,PICKUP_TIME,DEPART_TIME,ROUTE_ID,AP_TARRIF_ID,AUDIT_STAT,AUDIT_TIME,
              AUDITOR,PAY_STAT,PRE_PAY_TIME,PAY_TIME,PAYER,PRINT_FLAG,PRINT_TMS,ABNOMAL_STAT,SEAL_NO,TOT_QNTY,TOT_VOL,
              TOT_GROSS_W,TOT_WORTH,TRANS_UOM,CURRENT_ORG_ID,CURRENT_LOC,NOTES,ADDWHO,ADDTIME,EDITWHO,EDITTIME,
              AUDITED_FLAG,DONE_TIME,TOT_NET_W,ABNOMAL_NOTES,DISPATCH_STAT,SETT_MILE,TOT_QNTY_EACH,UDF1,UDF2,UDF3,UDF4,
              EXEC_ORG_ID_NAME,STATUS_NAME,DISPATCH_STAT_NAME,REMAIN_GROSS_W,REMAIN_VOL,MAX_GROSS_W,MAX_VOL,TEMPERATURE1,
              TEMPERATURE2,VEH_SIGN,LOAD_STAT,SIGN_ORG_ID,ACCOUNT_STAT,FEEAUDIT_STAT,ACCOUNT_TIME,FEEAUDIT_TIME,CHECK_FLAG,
              OP_PICKUP_TIME,GPS_NO1,GPS_NO2,TEMP_NO1,TEMP_NO2,LOAD_COUNT,UNLOAD_COUNT,PRE_UNLOAD_TIME,ARRIVE_WHSE_TIME,
              START_LOAD_TIME,END_LOAD_TIME,QUALIFIED_FLAG,AUDIT_NOTES,TOT_AMOUNT)
              
              select ID,LOAD_NO,STATUS,EXEC_ORG_ID,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER1,MOBILE1,
              DRVR_LIC_NUM,CONFIRMOR,CONFIRM_TIME,REF_NO,START_AREA_ID,START_AREA_NAME,END_AREA_ID,END_AREA_NAME,
              TRANS_SRVC_ID,START_ODOMETER,RETURN_ODOMETER,MILEAGE,EMPTY_MILEAGE,LADEN_MILEAGE,TRAILER_NO,DRIVER2,
              MOBILE2,PRE_PICKUP_TIME,PRE_DEPART_TIME,PICKUP_TIME,DEPART_TIME,ROUTE_ID,AP_TARRIF_ID,AUDIT_STAT,AUDIT_TIME,
              AUDITOR,PAY_STAT,PRE_PAY_TIME,PAY_TIME,PAYER,PRINT_FLAG,PRINT_TMS,ABNOMAL_STAT,SEAL_NO,TOT_QNTY,TOT_VOL,
              TOT_GROSS_W,TOT_WORTH,TRANS_UOM,CURRENT_ORG_ID,CURRENT_LOC,NOTES,ADDWHO,ADDTIME,EDITWHO,EDITTIME,
              AUDITED_FLAG,DONE_TIME,TOT_NET_W,ABNOMAL_NOTES,DISPATCH_STAT,SETT_MILE,TOT_QNTY_EACH,UDF1,UDF2,UDF3,UDF4,
              EXEC_ORG_ID_NAME,STATUS_NAME,DISPATCH_STAT_NAME,REMAIN_GROSS_W,REMAIN_VOL,MAX_GROSS_W,MAX_VOL,TEMPERATURE1,
              TEMPERATURE2,VEH_SIGN,LOAD_STAT,SIGN_ORG_ID,ACCOUNT_STAT,FEEAUDIT_STAT,ACCOUNT_TIME,FEEAUDIT_TIME,CHECK_FLAG,
              OP_PICKUP_TIME,GPS_NO1,GPS_NO2,TEMP_NO1,TEMP_NO2,LOAD_COUNT,UNLOAD_COUNT,PRE_UNLOAD_TIME,ARRIVE_WHSE_TIME,
              START_LOAD_TIME,END_LOAD_TIME,QUALIFIED_FLAG,AUDIT_NOTES,TOT_AMOUNT from trans_load_header where load_no = in_load_no;
              
              insert into BMS_SHIPMENT_HEADER(ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,ODR_TIME,
              LOAD_NO,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,
              PRE_LOAD_TIME,DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,
              UNLOAD_TEL,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,STATUS,BILL_TO,REFENENCE1,REFENENCE2,
              REFENENCE3,REFENENCE4,CREATE_ORG_ID,EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,COD_FLAG,TOT_QNTY,
              TRANS_UOM,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,
              SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,SPLIT_FLAG,
              SPLIT_REASON_CODE,SPLIT_REASON,ADDTIME,ADDWHO,EDITTIME,EDITWHO,ASSIGN_STAT,ABNOMAL_NOTES,LOSDAM_FLAG,
              START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,OP_LOAD_TIME,SIGNATARY,OP_UNLOAD_TIME,
              OP_POD_TIME,LOAD_SEQ,UNLOAD_SEQ,LOAD_DELAY_REASON,UNLOAD_DELAY_REASON,POD_DELAY_REASON,CURRENT_LOC,WHSE_ID,
              UNLOAD_DELAY_DAYS,POD_DELAY_DAYS,SATISFY_CODE,SERVICE_CODE,LOAD_PRINT_COUNT,SHPM_PRINT_COUNT,PRE_WHSE_TIME,
              PRE_ASSIGN_TIME,ASSIGN_TIME,PRE_END_LOAD_TIME,FACT_MILE,SETT_MILE,ROUTE_MILE,PICKING_STAT,TRACK_NOTES,
              LEAVE_WHSE_TIME,ARRIVE_WHSE_TIME,BELONG_JRNY_NO,FIRST_JRNY_NO,LAST_JRNY_NO,SENDABLE_FLAG,END_UNLOAD_ID,
              END_UNLOAD_NAME,END_UNLOAD_ADDRESS,RECE_FLAG,UNION_SHPM_NO,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,
              STATUS_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,
              UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,END_TEL,END_CONTACT,
              SIGN_ORG_ID_NAME,BIZ_TYP,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,LOAD_REGION,
              UNLOAD_REGION,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,
              TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME)
              select ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,ODR_TIME,
              LOAD_NO,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,
              PRE_LOAD_TIME,DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,
              UNLOAD_TEL,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,STATUS,BILL_TO,REFENENCE1,REFENENCE2,
              REFENENCE3,REFENENCE4,CREATE_ORG_ID,EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,COD_FLAG,TOT_QNTY,
              TRANS_UOM,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,
              SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,SPLIT_FLAG,
              SPLIT_REASON_CODE,SPLIT_REASON,ADDTIME,ADDWHO,EDITTIME,EDITWHO,ASSIGN_STAT,ABNOMAL_NOTES,LOSDAM_FLAG,
              START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,OP_LOAD_TIME,SIGNATARY,OP_UNLOAD_TIME,
              OP_POD_TIME,LOAD_SEQ,UNLOAD_SEQ,LOAD_DELAY_REASON,UNLOAD_DELAY_REASON,POD_DELAY_REASON,CURRENT_LOC,WHSE_ID,
              UNLOAD_DELAY_DAYS,POD_DELAY_DAYS,SATISFY_CODE,SERVICE_CODE,LOAD_PRINT_COUNT,SHPM_PRINT_COUNT,PRE_WHSE_TIME,
              PRE_ASSIGN_TIME,ASSIGN_TIME,PRE_END_LOAD_TIME,FACT_MILE,SETT_MILE,ROUTE_MILE,PICKING_STAT,TRACK_NOTES,
              LEAVE_WHSE_TIME,ARRIVE_WHSE_TIME,BELONG_JRNY_NO,FIRST_JRNY_NO,LAST_JRNY_NO,SENDABLE_FLAG,END_UNLOAD_ID,
              END_UNLOAD_NAME,END_UNLOAD_ADDRESS,RECE_FLAG,UNION_SHPM_NO,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,
              STATUS_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,
              UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,END_TEL,END_CONTACT,
              SIGN_ORG_ID_NAME,BIZ_TYP,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,LOAD_REGION,
              UNLOAD_REGION,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,
              TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME
              from TRANS_SHIPMENT_HEADER where load_no = in_load_no;*/
              --insert into tmp_order_header select * from trans_order_header where odr_no = t_odr_no;
              --insert into tmp_order_item select * from trans_order_item where odr_no = t_odr_no;
           
              output_result := output_result || RDPG_STAT.TRANS_DEPART || to_char(t_max_time,'yyyy-MM-dd HH24:mi');
          else
              --更新调度单状态为部分发货
              --yuanlei 2012-09-13 解决部分到货状态调度单下的作业单发运时，调度单状态变成部分发运的BUG
              --UPDATE TRANS_LOAD_HEADER SET status=RDPG_STAT.TRANS_PART_DEPART, edittime = sysdate,editwho = in_user_id
              --  WHERE load_no =in_load_no;
              --output_result := output_result || RDPG_STAT.TRANS_PART_DEPART;
              if op_status < RDPG_STAT.TRANS_PART_DEPART then
                  UPDATE TRANS_LOAD_HEADER SET status=RDPG_STAT.TRANS_PART_DEPART,status_name=RDPG_STAT.TRANS_PART_DEPART_NAME, edittime = sysdate,editwho = in_user_id
                    WHERE load_no =in_load_no;
                  output_result := output_result || RDPG_STAT.TRANS_PART_DEPART;
              else
                  output_result := output_result || op_status;
              end if;
              --yuanlei
          end if;
          --插入业务日志
          insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
            values(sys_guid(),RDPG_STAT.SEND,RDPG_STAT.LOAD_NO,in_load_no,'作业单:' || op_notes || '发货确认',sysdate,in_user_id);


      end if;
      --统计里程,金额插入到结算清单表
      --SP_LOADNO_TOTAL_MANIFEST(in_load_no, in_load_time, in_user_id, v_output_result);
      if op_tempeq1 is not null then
          update bas_tempeq set status = '3A60CB9A86884871A8C8EF734B1973F6',LOAD_NO=in_load_no,PLATE_NO = op_plate_no where id = op_tempeq1;
          select equip_no into op_equip1 from bas_tempeq where id = op_tempeq1;
          if op_tempeq2 is not null then
               select equip_no into op_equip2 from bas_tempeq where id = op_tempeq2;
              op_notes := '[' || op_equip1 || ']和[' || op_equip2 || ']随车[' || op_plate_no || ']使用!';
          else
              op_notes := '[' || op_equip1 || ']随车[' || op_plate_no || ']使用!';
          end if;
          insert into bas_temp_records(id,load_no,plate_no,suplr_name,driver,mobile,load_address,unload_address,addtime,addwho,equip_no,notes)
              values(sys_guid(),in_load_no,op_plate_no,op_suplr_name,op_driver,op_mobile,op_start_area,op_end_area,sysdate,in_user_id,op_tempeq1,op_notes);
      end if;
      if op_tempeq2 is not null then
          update bas_tempeq set status = '3A60CB9A86884871A8C8EF734B1973F6',LOAD_NO=in_load_no,PLATE_NO = op_plate_no where id = op_tempeq2;
          select equip_no into op_equip2 from bas_tempeq where id = op_tempeq2;
           if op_tempeq1 is not null then
              select equip_no into op_equip1 from bas_tempeq where id = op_tempeq1;
              op_notes := '[' || op_equip1 || ']和[' || op_equip2 || ']随车[' || op_plate_no || ']使用!';
          else
              op_notes := '[' || op_equip1 || ']随车[' || op_plate_no || ']使用!';
          end if;
          insert into bas_temp_records(id,load_no,plate_no,suplr_name,driver,mobile,load_address,unload_address,addtime,addwho,equip_no,notes)
              values(sys_guid(),in_load_no,op_plate_no,op_suplr_name,op_driver,op_mobile,op_start_area,op_end_area,sysdate,in_user_id,op_tempeq2,op_notes);
      end if;
      if op_gps_no is not null and length(op_gps_no) > 0 then
          update bas_gpseq set status = '3A60CB9A86884871A8C8EF734B1973F6',LOAD_NO=in_load_no,PLATE_NO = op_plate_no where id = op_gps_no;
          select equip_no into op_equip3 from bas_gpseq where id = op_gps_no;
          op_notes := '[' || op_equip3 || ']随车[' || op_plate_no || ']使用!';
          insert into bas_gps_records(id,load_no,plate_no,suplr_name,driver,mobile,load_address,unload_address,addtime,addwho,equip_no,notes)
              values(sys_guid(),in_load_no,op_plate_no,op_suplr_name,op_driver,op_mobile,op_start_area,op_end_area,sysdate,in_user_id,op_gps_no,op_notes);
      end if;
      if length(v_output_result) > 2 then
         output_result := output_result || substr(v_output_result,2);
         return;
      end if;
    commit;
      EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result:='01'||sqlerrm;
      return;
  END;
/

6?	AA4F32C3168447D18782AEF6F241C72F	99	已关闭	已关闭	6	Y	N			2010/11/25 11:05:23	wpsadmin	TRANS_LOAD_STAT	wpsadmin	2010/11/25 11:37:48=======
6?	AA4F32C3168447D18782AEF6F241C72F	99	已关闭	已关闭	6	Y	N			2010/11/25 11:05:23	wpsadmin	TRANS_LOAD_STAT	wpsadmin	2010/11/25 11:37:48>>>>>>> .r1382

--caijiante 2017-5-16
--sys_func_page
968?	22969	P09_F036_P0_11	P09_F036_P0	导出作业单	150	Y	2015/5/27	wpsadmin	导出作业单	B	
969?	22970	P09_F036_P0_12	P09_F036_P0	导出调度单	150	Y	2015/5/27	wpsadmin	导出调度单	B	
970?	22971	P06_T039_P0_05	P06_T039_P0	取消换车	150	Y	2015/5/27	wpsadmin	取消换车	T	
972?	22973	P06_T051_P0	P06_T051	审批设置	150	Y	2015/5/27	wpsadmin	审批设置	B	
973?	22974	P06_T051_P2	P06_T051	指定客户	150	Y	2015/5/27	wpsadmin	指定客户	B	
974?	22975	P06_T051_P2_01	P06_T051_P2	保存	150	Y	2015/5/27	wpsadmin	保存	B	
975?	22976	P06_T051_P2_02	P06_T051_P2	取消	150	Y	2015/5/27	wpsadmin	取消	B	

update sys_func_page set parent_function_id = 'P06_T051_P0' where function_id = 'P06_T051_P1_01'
update sys_func_page set parent_function_id = 'P06_T051_P0' where function_id = 'P06_T051_P1_02'


--yuanlei 2017-5-17
create or replace procedure SP_LOAD_CHANGE_RECORD
(
 in_load_no varchar2, --调度单号
 in_shpm_no LST,   --作业单
 in_fm_suplr_id varchar2,   --承运商
 in_fm_plate_no varchar2,   --原车牌号
 in_fm_vehicle_typ varchar2,   --原车辆类型
 in_fm_driver varchar2,   --原司机
 in_fm_mobile varchar2,   --原手机
 in_suplr_id varchar2,     --新承运商
 in_plate_no varchar2,      --车牌号
 in_vehicle_typ varchar2,   --车辆类型
 in_driver varchar2,        --司机
 in_mobile varchar2,        --手机
 in_card_no1 varchar2,
 in_card_no2 varchar2,
 in_card_no3 varchar2,
 in_card_no4 varchar2,
 in_change_time varchar2,  --换车时间
 in_change_area varchar2,  --换车地点
 in_change_reason varchar2, --换车原因
 in_notes varchar2,         --备注
 in_user_id varchar2,       --用户
 output_result OUT VARCHAR2
 )
 IS
 op_new_load_no varchar2(100);

 i number(8);
 t_act_result varchar(1024);
 sys_shpm varchar(2048);

 tmp_shpm_no varchar2(100);
 fm_suplr_name varchar2(100);
 tmp_suplr_name varchar2(100);
 ----DEBUG----
 --in_shpm_no LST := LST();
 /*in_odr_no LST := LST();
 in_plate_no LST := LST();
 in_vehicle_typ LST := LST();
 in_driver LST := LST();
 in_mobile LST := LST();*/
 ----DEBUG----

 op_status varchar2(50);

 CURSOR shpm IS
      SELECT shpm_no FROM trans_shipment_header 
      where load_no in (select load_no2 from trans_change_record WHERE load_no1 = in_load_no);
 CURSOR item IS
      SELECT shpm_no FROM trans_shipment_header where load_no = in_load_no;
         
 BEGIN
    output_result := '00';

    ----DEBUG----
    --dbms_output.put_line('start:' || to_char(sysdate,'yyyy-mm-dd hh:mi:ss'));
    --in_shpm_no.extend;
    --in_shpm_no(1) := '2017022700001_S1';
    /*in_shpm_no.extend;
    in_shpm_no(2) := '201409100008';
    in_odr_no.extend;
    in_odr_no(1) := '201409100007';                                
    in_odr_no.extend;
    in_odr_no(2) := '201409100008';
    in_plate_no.extend;
    in_plate_no(1) := null;
    in_vehicle_typ.extend;
    in_vehicle_typ(1) := null;
    in_driver.extend;
    in_driver(1) := null;
    in_mobile.extend;
    in_mobile(1) := null;*/
    ----DEBUG----

    select status into op_status from trans_load_header where load_no = in_load_no;
    select short_name into tmp_suplr_name from bas_supplier where id = in_suplr_id;
    if op_status < '40' then
        output_result := '01调度单未发运，请直接修改调度单信息!';
        return;
    end if;
    if op_status > '50' then
      output_result := '01调度单已签收，不允许做换车记录!';
      return;
    end if;
    
    sys_shpm := '';
    for h in shpm loop
       tmp_shpm_no := h.shpm_no;
       sys_shpm  := sys_shpm || ',' || tmp_shpm_no ;
    end loop;
    
    if trim(in_shpm_no(1)) is null then
        for h in item loop
            tmp_shpm_no := h.shpm_no;
            if instr(sys_shpm,tmp_shpm_no) > 0 then
                output_result := '01作业单[' || tmp_shpm_no || ']已做过换车记录，不允许做换车记录!';
                return;
            end if;
        end loop;
    else
        for   i   in   1..in_shpm_no.count   loop

            tmp_shpm_no := in_shpm_no(i);
            if instr(sys_shpm,tmp_shpm_no) > 0 then
                output_result := '01作业单[' || tmp_shpm_no || ']已做过换车记录，不允许做换车记录!';
                return;
            end if;
        end loop;
    end if;

    begin
        select load_no2 into op_new_load_no from trans_change_record where load_no1 = in_load_no;
    exception when no_data_found then
        op_new_load_no := in_load_no;
    end;
    
    op_new_load_no := op_new_load_no || '_1';
    
    select suplr_cname into fm_suplr_name from bas_supplier where id = in_fm_suplr_id; 
    select suplr_cname into tmp_suplr_name from bas_supplier where id = in_suplr_id;

    insert into trans_load_header(id,load_no,status,exec_org_id,suplr_id,suplr_name,vehicle_typ_id,plate_no,driver1,mobile1,drvr_lic_num
    ,confirmor,confirm_time,ref_no,start_area_id,start_area_name,end_area_id,end_area_name,trans_srvc_id,start_odometer,return_odometer,mileage
    ,empty_mileage,laden_mileage,trailer_no,driver2,mobile2,pre_pickup_time,pre_depart_time,pickup_time,depart_time,route_id,ap_tarrif_id
    ,audit_stat,audit_time,auditor,pay_stat,pre_pay_time,pay_time,payer,print_flag,print_tms,abnomal_stat,seal_no,tot_qnty,tot_vol
    ,tot_gross_w,tot_worth,current_org_id,current_loc,notes,audited_flag,done_time,tot_net_w,abnomal_notes,dispatch_stat,sett_mile
    ,tot_qnty_each,udf1,udf2,udf3,udf4,exec_org_id_name,status_name,dispatch_stat_name,remain_gross_w,remain_vol,max_gross_w
    ,max_vol,temperature1,temperature2,veh_sign,load_stat,sign_org_id,account_stat,feeaudit_stat,account_time,feeaudit_time,addwho,addtime)
    select sys_guid(),op_new_load_no,status,exec_org_id,in_suplr_id,tmp_suplr_name,in_vehicle_typ,in_plate_no,in_driver,in_mobile,drvr_lic_num
    ,confirmor,confirm_time,ref_no,start_area_id,start_area_name,end_area_id,end_area_name,trans_srvc_id,start_odometer,return_odometer,mileage
    ,empty_mileage,laden_mileage,trailer_no,driver2,mobile2,pre_pickup_time,pre_depart_time,pickup_time,depart_time,route_id,ap_tarrif_id
    ,audit_stat,audit_time,auditor,pay_stat,pre_pay_time,pay_time,payer,print_flag,print_tms,abnomal_stat,seal_no,tot_qnty,tot_vol
    ,tot_gross_w,tot_worth,current_org_id,current_loc,notes,audited_flag,done_time,tot_net_w,abnomal_notes,dispatch_stat,sett_mile
    ,tot_qnty_each,udf1,udf2,udf3,udf4,exec_org_id_name,status_name,dispatch_stat_name,remain_gross_w,remain_vol,max_gross_w
    ,max_vol,temperature1,temperature2,veh_sign,load_stat,sign_org_id,account_stat,feeaudit_stat,account_time,feeaudit_time,in_user_id,sysdate
    from trans_load_header where load_no = in_load_no;
    
    t_act_result := '调度单[' || in_load_no || ']部分换车生成新调度单!'; 
    --插入业务日志
    insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
        values(sys_guid(),RDPG_STAT.CHANGE_VEH,RDPG_STAT.LOAD_NO,op_new_load_no,t_act_result,sysdate,in_user_id);
        
    t_act_result := '调度单换车，车牌号由[' || in_fm_plate_no || ']换成[' || in_plate_no || ']!';
    --插入业务日志
    insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
        values(sys_guid(),RDPG_STAT.CHANGE_VEH,RDPG_STAT.LOAD_NO,in_load_no,t_act_result,sysdate,in_user_id);   
        
    if trim(in_shpm_no(1)) is null then --换整车
        insert into trans_change_record(id,suplr_id1,load_no1,plate_no1,vehicle_typ_id1,driver1,mobile1,suplr_id2,load_no2,plate_no2,vehicle_typ_id2
          ,driver2,mobile2,card_no1,card_no2,card_no3,card_no4,change_time,change_area_id,change_reason,notes,addtime,addwho)
        values(sys_guid(),in_fm_suplr_id,in_load_no,in_fm_plate_no,in_fm_vehicle_typ,in_fm_driver,in_fm_mobile,in_suplr_id,op_new_load_no,in_plate_no,in_vehicle_typ
          ,in_driver,in_mobile,in_card_no1,in_card_no2,in_card_no3,in_card_no4,to_date(in_change_time,'yyyy-MM-dd HH24:mi:ss')
          ,in_change_area,in_change_reason,in_notes,sysdate,in_user_id);
              
        insert into TRANS_SHIPMENT_HEADER(
        ID,LOAD_NO,SHPM_NO,STATUS,ODR_NO,PARN_SHPM_NO,SENDABLE_FLAG,SPLIT_FLAG,SPLIT_REASON_CODE,SPLIT_REASON,ADDWHO,ADDTIME,
        CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,TRANS_SRVC_ID_NAME,ODR_TYP,ASSIGN_STAT,ASSIGN_TIME,FACT_MILE,ARRIVE_WHSE_TIME,
        ODR_TIME,FROM_LOAD_TIME,PRE_LOAD_TIME,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,PRE_POD_TIME,DEPART_TIME,START_LOAD_TIME,SETT_MILE,
        LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_REGION,UNLOAD_REGION,PRE_ASSIGN_TIME,
        STATUS_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,UDF5,UDF6,POD_FLAG,COD_FLAG,ABNOMAL_NOTES,PRE_WHSE_TIME,
        CREATE_ORG_ID,CREATE_ORG_ID_NAME,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,BIZ_CODE,LOAD_STAT,LOSDAM_FLAG,LOAD_SEQ,UNLOAD_SEQ,
        TRANS_UOM,TOT_QNTY,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,UNLOAD_ZIP,SOURCE_NO,FREEZE_NOTES,
        PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,OP_LOAD_TIME,BELONG_JRNY_NO,
        DRIVER,MOBILE,ABNOMAL_STAT,WHSE_ID,END_UNLOAD_ID,END_UNLOAD_NAME,END_UNLOAD_ADDRESS,END_CONTACT,END_LOAD_TIME,TRACK_NOTES,
        END_TEL,BIZ_TYP,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_CODE,ROUTE_MILE,LEAVE_WHSE_TIME,
        UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,PICKING_STAT,
        UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,SIGN_ORG_ID,SIGN_ORG_ID_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME,
        FIRST_JRNY_NO,LAST_JRNY_NO,RECE_FLAG,UNION_SHPM_NO,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,UDF7,UDF8,
        VEH_POS,BUK_FLAG,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,TRANS_TYPE,OP_PICKUP_TIME,Temperature1,Temperature2,upload_flag,start_unload_time,CAST_BILL_TIME)
        select sys_guid(),op_new_load_no,SHPM_NO||'_1',STATUS,ODR_NO,PARN_SHPM_NO,SENDABLE_FLAG,SPLIT_FLAG,SPLIT_REASON_CODE,SPLIT_REASON,ADDWHO,ADDTIME,
        CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,TRANS_SRVC_ID_NAME,ODR_TYP,ASSIGN_STAT,ASSIGN_TIME,FACT_MILE,ARRIVE_WHSE_TIME,
        ODR_TIME,FROM_LOAD_TIME,PRE_LOAD_TIME,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,PRE_POD_TIME,DEPART_TIME,START_LOAD_TIME,SETT_MILE,
        LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_REGION,UNLOAD_REGION,PRE_ASSIGN_TIME,
        STATUS_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,UDF5,UDF6,POD_FLAG,COD_FLAG,ABNOMAL_NOTES,PRE_WHSE_TIME,
        CREATE_ORG_ID,CREATE_ORG_ID_NAME,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,BIZ_CODE,LOAD_STAT,LOSDAM_FLAG,LOAD_SEQ,UNLOAD_SEQ,
        TRANS_UOM,TOT_QNTY,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,UNLOAD_ZIP,SOURCE_NO,FREEZE_NOTES,
        PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,in_suplr_id,tmp_suplr_name,in_vehicle_typ,in_plate_no,OP_LOAD_TIME,BELONG_JRNY_NO,
        in_driver,in_mobile,ABNOMAL_STAT,WHSE_ID,END_UNLOAD_ID,END_UNLOAD_NAME,END_UNLOAD_ADDRESS,END_CONTACT,END_LOAD_TIME,TRACK_NOTES,
        END_TEL,BIZ_TYP,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_CODE,ROUTE_MILE,LEAVE_WHSE_TIME,
        UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,PICKING_STAT,
        UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,SIGN_ORG_ID,SIGN_ORG_ID_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME,
        FIRST_JRNY_NO,LAST_JRNY_NO,RECE_FLAG,UNION_SHPM_NO,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,UDF7,UDF8,
        VEH_POS,BUK_FLAG,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,TRANS_TYPE,OP_PICKUP_TIME,Temperature1,Temperature2,upload_flag,start_unload_time,CAST_BILL_TIME
        from TRANS_SHIPMENT_HEADER WHERE LOAD_NO = in_load_no;
        
        --update TRANS_SHIPMENT_HEADER SET STATUS = RDPG_STAT.SHPM_CLOSED,STATUS_NAME = RDPG_STAT.SHPM_CLOSED_NAME where LOAD_NO = in_load_no;
        --update TRANS_LOAD_HEADER SET STATUS = RDPG_STAT.SHPM_CLOSED,STATUS_NAME = RDPG_STAT.SHPM_CLOSED_NAME where LOAD_NO = in_load_no;
    else
        for   i   in   1..in_shpm_no.count   loop

            tmp_shpm_no := in_shpm_no(i);
            --循环更新作业单的调度单号、状态、装货顺序和卸货顺序
            --update TRANS_SHIPMENT_HEADER SET LOAD_NO = op_new_load_no,PLATE_NO = in_plate_no,VEHICLE_TYP_ID = in_vehicle_typ
            --   ,DRIVER = in_driver,MOBILE= in_mobile where SHPM_NO = tmp_shpm_no;
            t_act_result := '换车记录,生成调度单' || op_new_load_no || '],车牌号[' || in_plate_no || ']';
            --插入作业单业务日志
            insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
                values(sys_guid(),RDPG_STAT.DISPATCH,RDPG_STAT.SHPM_NO,tmp_shpm_no,t_act_result,sysdate,in_user_id);
               
            insert into TRANS_SHIPMENT_HEADER(
            ID,LOAD_NO,SHPM_NO,STATUS,ODR_NO,PARN_SHPM_NO,SENDABLE_FLAG,SPLIT_FLAG,SPLIT_REASON_CODE,SPLIT_REASON,ADDWHO,ADDTIME,
            CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,TRANS_SRVC_ID_NAME,ODR_TYP,ASSIGN_STAT,ASSIGN_TIME,FACT_MILE,ARRIVE_WHSE_TIME,
            ODR_TIME,FROM_LOAD_TIME,PRE_LOAD_TIME,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,PRE_POD_TIME,DEPART_TIME,START_LOAD_TIME,SETT_MILE,
            LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_REGION,UNLOAD_REGION,PRE_ASSIGN_TIME,
            STATUS_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,UDF5,UDF6,POD_FLAG,COD_FLAG,ABNOMAL_NOTES,PRE_WHSE_TIME,
            CREATE_ORG_ID,CREATE_ORG_ID_NAME,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,BIZ_CODE,LOAD_STAT,LOSDAM_FLAG,LOAD_SEQ,UNLOAD_SEQ,
            TRANS_UOM,TOT_QNTY,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,UNLOAD_ZIP,SOURCE_NO,FREEZE_NOTES,
            PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,OP_LOAD_TIME,BELONG_JRNY_NO,
            DRIVER,MOBILE,ABNOMAL_STAT,WHSE_ID,END_UNLOAD_ID,END_UNLOAD_NAME,END_UNLOAD_ADDRESS,END_CONTACT,END_LOAD_TIME,TRACK_NOTES,
            END_TEL,BIZ_TYP,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_CODE,ROUTE_MILE,LEAVE_WHSE_TIME,
            UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,PICKING_STAT,
            UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,SIGN_ORG_ID,SIGN_ORG_ID_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME,
            FIRST_JRNY_NO,LAST_JRNY_NO,RECE_FLAG,UNION_SHPM_NO,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,UDF7,UDF8,
            VEH_POS,BUK_FLAG,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,TRANS_TYPE,OP_PICKUP_TIME,Temperature1,Temperature2,upload_flag,start_unload_time,CAST_BILL_TIME)
            select sys_guid(),op_new_load_no,SHPM_NO||'_1',STATUS,ODR_NO,PARN_SHPM_NO,SENDABLE_FLAG,SPLIT_FLAG,SPLIT_REASON_CODE,SPLIT_REASON,ADDWHO,ADDTIME,
            CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,TRANS_SRVC_ID_NAME,ODR_TYP,ASSIGN_STAT,ASSIGN_TIME,FACT_MILE,ARRIVE_WHSE_TIME,
            ODR_TIME,FROM_LOAD_TIME,PRE_LOAD_TIME,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,PRE_POD_TIME,DEPART_TIME,START_LOAD_TIME,SETT_MILE,
            LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_REGION,UNLOAD_REGION,PRE_ASSIGN_TIME,
            STATUS_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,UDF5,UDF6,POD_FLAG,COD_FLAG,ABNOMAL_NOTES,PRE_WHSE_TIME,
            CREATE_ORG_ID,CREATE_ORG_ID_NAME,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,BIZ_CODE,LOAD_STAT,LOSDAM_FLAG,LOAD_SEQ,UNLOAD_SEQ,
            TRANS_UOM,TOT_QNTY,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,UNLOAD_ZIP,SOURCE_NO,FREEZE_NOTES,
            PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,in_suplr_id,tmp_suplr_name,in_vehicle_typ,in_plate_no,OP_LOAD_TIME,BELONG_JRNY_NO,
            in_driver,in_mobile,ABNOMAL_STAT,WHSE_ID,END_UNLOAD_ID,END_UNLOAD_NAME,END_UNLOAD_ADDRESS,END_CONTACT,END_LOAD_TIME,TRACK_NOTES,
            END_TEL,BIZ_TYP,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_CODE,ROUTE_MILE,LEAVE_WHSE_TIME,
            UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,PICKING_STAT,
            UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,SIGN_ORG_ID,SIGN_ORG_ID_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME,
            FIRST_JRNY_NO,LAST_JRNY_NO,RECE_FLAG,UNION_SHPM_NO,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,UDF7,UDF8,
            VEH_POS,BUK_FLAG,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,TRANS_TYPE,OP_PICKUP_TIME,Temperature1,Temperature2,upload_flag,start_unload_time,CAST_BILL_TIME
            from TRANS_SHIPMENT_HEADER WHERE SHPM_NO = tmp_shpm_no;
        end   loop;
        

        --汇总毛重、体积、净重、数量、货值
        UPDATE TRANS_LOAD_HEADER SET (TOT_GROSS_W, TOT_NET_W, TOT_VOL, TOT_WORTH, TOT_QNTY, TOT_QNTY_EACH, Temperature1, Temperature2)
          = ( select SUM(TOT_GROSS_W) as SUM_GROSS_W,SUM(TOT_NET_W) AS SUM_NET_W
              ,SUM(TOT_VOL) AS SUM_VOL,SUM(TOT_WORTH) AS SUM_WORTH,SUM(TOT_QNTY) AS SUM_QNTY,SUM(TOT_QNTY_EACH) AS SUM_QNTY_EACH,MAX(REFENENCE4),MIN(REFENENCE4)
              from TRANS_SHIPMENT_HEADER where TRANS_SHIPMENT_HEADER.LOAD_NO = TRANS_LOAD_HEADER.LOAD_NO
              group by LOAD_NO
             ) where LOAD_NO = op_new_load_no;
        
       insert into trans_change_record(id,suplr_id1,load_no1,plate_no1,vehicle_typ_id1,driver1,mobile1,suplr_id2,load_no2,plate_no2,vehicle_typ_id2
          ,driver2,mobile2,card_no1,card_no2,card_no3,card_no4,change_time,change_area_id,change_reason,notes,addtime,addwho)
        values(sys_guid(),in_fm_suplr_id,in_load_no,in_fm_plate_no,in_fm_vehicle_typ,in_fm_driver,in_fm_mobile,in_suplr_id,op_new_load_no,in_plate_no,in_vehicle_typ
          ,in_driver,in_mobile,in_card_no1,in_card_no2,in_card_no3,in_card_no4,to_date(in_change_time,'yyyy-MM-dd HH24:mi:ss')
          ,in_change_area,in_change_reason,in_notes,sysdate,in_user_id);

    end if;
    commit;

    output_result := '00换车记录操作成功，生成新调度单[' || op_new_load_no || ']！';

    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result:='01'||sqlerrm;
           --output_result :=op_load_no;
    return;
 END;

/

create or replace procedure SP_LOAD_CANCEL_CHANGE
(
 in_load_no varchar2, --调度单号
 in_user_id varchar2,       --用户
 output_result OUT VARCHAR2
 )
 IS
 op_load_no varchar2(100);
 op_load_no1 varchar2(100);

 op_status varchar2(50);

 BEGIN
    output_result := '00';
    begin
        select load_no1 into op_load_no from trans_change_record where load_no2 = in_load_no;
    exception when no_data_found then
        op_load_no := null;
    end;
    if op_load_no is null then
        output_result := '01调度单[' || in_load_no || ']没有换车记录，不需要取消换车!';
        return;
    end if;
    begin
       select load_no2 into op_load_no1 from trans_change_record where load_no1 = in_load_no;
    exception when no_data_found then
       op_load_no1 := null;
    end;
    if op_load_no1 is not null then
        output_result := '00调度单已做过换车操作，请先将调度单[' || op_load_no1 || ']取消换车！';
        return;
    end if;
    
    select status into op_status from trans_load_header where load_no = in_load_no;
    if op_status >= '45' then
        output_result := '01调度单[' || in_load_no || ']已签收,不能取消换车，请先取消签收!';
        return;
    end if;
            
    delete from trans_shipment_header where load_no = in_load_no;
    delete from trans_load_header where load_no = in_load_no;
    delete from trans_change_record where load_no2 = in_load_no;
        
    --插入业务日志
    insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
        values(sys_guid(),RDPG_STAT.CHANGE_VEH,RDPG_STAT.LOAD_NO,in_load_no,'取消换车,删除调度单[' || in_load_no || ']',sysdate,in_user_id);

    commit;
    
    output_result := '00取消换车成功！';
    
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result:='01'||sqlerrm;
           --output_result :=op_load_no;
    return;
 END;

/

create or replace procedure SP_SHPM_CANCELSPLIT_CHANGEQNTY(
in_parn_shpm_no VARCHAR2,  --上级作业单号
in_shpm_no VARCHAR2,      --作业单号
in_user_id VARCHAR2,      --用户ID
output_result OUT VARCHAR2
)
/*
 *     取消分量拆分
 *     yuanlei
 *     2010-12-4
 */
IS
t_other_shpm_no varchar(100);
t_status varchar(2);
t_status_name varchar(10);
t_count number(4);
t_load_no varchar2(100);
BEGIN
        output_result:='00';
        select status,shpm_no,load_no into t_status,t_other_shpm_no,t_load_no from TRANS_SHIPMENT_HEADER t_header
        WHERE PARN_SHPM_NO != SHPM_NO AND PARN_SHPM_NO = in_parn_shpm_no and SHPM_NO = in_shpm_no;
        /*if t_status <> RDPG_STAT.SHPM_CONFIRM then
            begin
                select name_c into t_status_name from BAS_CODES where PROP_CODE = RDPG_STAT.SHPMNO_STAT AND CODE = t_status;
                output_result := '01'|| '作业单['|| t_other_shpm_no || ']' || t_status_name || ',不能取消拆分!';
                return;
            end;
        end if;*/

        select status,shpm_no into t_status,t_other_shpm_no from TRANS_SHIPMENT_HEADER t_header
        WHERE PARN_SHPM_NO != SHPM_NO AND PARN_SHPM_NO = in_parn_shpm_no and SHPM_NO != in_shpm_no and rownum <= 1;
        if t_status <> RDPG_STAT.SHPM_LOAD then
            begin
                select name_c into t_status_name from BAS_CODES where PROP_CODE = RDPG_STAT.SHPMNO_STAT AND CODE = t_status;
                output_result := '01'|| '作业单['|| t_other_shpm_no || ']' || t_status_name || ',不能取消拆分!';
                return;
            end;
        end if;
        --删除所有子作业单的头记录
        delete from TRANS_SHIPMENT_HEADER WHERE PARN_SHPM_NO = in_parn_shpm_no AND PARN_SHPM_NO != SHPM_NO;
        --删除所有子作业单的明细记录
        delete from TRANS_SHIPMENT_ITEM WHERE PARN_SHPM_NO = in_parn_shpm_no AND PARN_SHPM_NO != SHPM_NO;
        --更新上级作业单头状态、拆分原因、拆分原因描述
        update TRANS_SHIPMENT_HEADER SET STATUS = RDPG_STAT.SHPM_CONFIRM,STATUS_NAME = RDPG_STAT.SHPM_CONFIRM_NAME,SPLIT_REASON_CODE = '',SPLIT_REASON = '',edittime = sysdate,LOAD_NO = t_load_no where SHPM_NO = in_parn_shpm_no;

        --插入业务日志
        insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
        values(sys_guid(),RDPG_STAT.CANSPIT_QNTY,RDPG_STAT.SHPM_NO,in_shpm_no,'取消拆分，作业单[' || in_parn_shpm_no || ']还原',sysdate,in_user_id);
        commit;
        EXCEPTION
           WHEN OTHERS THEN
           rollback;
           output_result:='01'||sqlerrm;
        return;
END;

/


create or replace procedure SP_SHPM_SPLIT_CHANGEQNTY(
in_shpm_no VARCHAR2,      --作业单号
in_shpm_row LST,          --作业单行号
in_shpm_qnty LST,         --明细数量
in_shpm_vol LST,          --明细体积
in_shpm_gw LST,           --明细毛重
in_shpm_nw LST,           --明细净重
in_shpm_worth LST,        --明细货值
in_split_reason VARCHAR2, --拆分原因
in_split_notes VARCHAR2,  --拆分备注
in_user_id VARCHAR2,      --用户ID
output_result OUT VARCHAR2
)
/*
 *     分量拆分
 *     yuanlei
 *     2010-12-4
 */
IS
t_cur_row number(8);
t_cur_qnty number(18,8);
t_sum_qnty number(18,8);
t_cur_vol number(18,8);
t_sum_vol number(18,8);
t_cur_gw number(18,8);
t_sum_gw number(18,8);
t_cur_nw number(18,8);
t_sum_nw number(18,8);
t_cur_worth number(18,8);
t_sum_worth number(18,8);
t_cur_each_qnty number(18,8);
t_sum_each_qnty number(18,8);
t_other_row varchar(200);
t_sendable_flag char(1);
--in_shpm_row LST := LST();
--in_shpm_qnty LST := LST();
--in_shpm_vol LST := LST();
--in_shpm_gw LST := LST();
--in_shpm_nw LST := LST();
--in_shpm_worth LST := LST();
t_sql varchar2(8096);
t_new_no1 varchar(200);
t_new_no2 varchar(200);
t_odr_no varchar2(200);
t_status varchar2(32);
t_status_name varchar2(100);
t_load_no varchar2(100);
BEGIN
        output_result:='00';
        /*in_shpm_row.extend;
        in_shpm_row(1) := '1';
        --in_shpm_row.extend;
        --in_shpm_row(2) := '3';
        --in_shpm_row.extend;
        --in_shpm_row(3) := '4';
        in_shpm_qnty.extend;
        in_shpm_qnty(1) := '16';
        in_shpm_qnty.extend;
        in_shpm_qnty(2) := '200';
        in_shpm_qnty.extend;
        in_shpm_qnty(3) := '300';
        in_shpm_vol.extend;
        in_shpm_vol(1) := '6.864';
        in_shpm_vol.extend;
        in_shpm_vol(2) := '7.872';
        in_shpm_vol.extend;
        in_shpm_vol(3) := '14.136';
        in_shpm_gw.extend;
        in_shpm_gw(1) := '1.281';
        in_shpm_gw.extend;
        in_shpm_gw(2) := '2.05';
        in_shpm_gw.extend;
        in_shpm_gw(3) := '2.553';
        in_shpm_nw.extend;
        in_shpm_nw(1) := '0';
        in_shpm_nw.extend;
        in_shpm_nw(2) := '0';
        in_shpm_nw.extend;
        in_shpm_nw(3) := '0';
        in_shpm_worth.extend;
        in_shpm_worth(1) := '0';
        in_shpm_worth.extend;
        in_shpm_worth(2) := '0';
        in_shpm_worth.extend;
        in_shpm_worth(3) := '0';*/

        select status,status_name,odr_no,sendable_flag,load_no into t_status,t_status_name,t_odr_no,t_sendable_flag,t_load_no from trans_shipment_header where shpm_no = in_shpm_no;
        /*if t_status <> rdpg_stat.SHPM_CONFIRM then
           output_result :='01'|| '只有【未调度】状态才能拆分作业单!';
           return;
        end if;*/
        if t_sendable_flag <> 'Y' then
           output_result :='01'|| '当前作业单上一段未签收，不允许拆分!';
           return;
        end if;

        t_new_no1 := in_shpm_no || '_S1';
        t_new_no2 := in_shpm_no || '_S2';
        --更新被拆分作业单状态
        update TRANS_SHIPMENT_HEADER SET STATUS = '92',STATUS_NAME = RDPG_STAT.SHPM_SPLITED_NAME --RDPG_STAT.SHPM_SPLITED
        ,SPLIT_REASON_CODE = in_split_reason,LOAD_NO = null,SPLIT_REASON = in_split_notes
        where SHPM_NO = in_shpm_no;

        --插入新作业单头
        insert into TRANS_SHIPMENT_HEADER(ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,
        ODR_TIME,LOAD_NO,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,PRE_LOAD_TIME,
        DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,FROM_UNLOAD_TIME,
        PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,STATUS,STATUS_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,CREATE_ORG_ID,
        EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,COD_FLAG,TRANS_UOM,TOT_QNTY,BIZ_TYP,LOAD_REGION,UNLOAD_REGION,
        TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,
        SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,SPLIT_FLAG,SPLIT_REASON_CODE,SPLIT_REASON,
        ASSIGN_STAT,ABNOMAL_NOTES,Losdam_Flag,START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,
        OP_LOAD_TIME,SIGNATARY,Op_Unload_Time,Op_Pod_Time,LOAD_SEQ,Unload_Seq,LOAD_DELAY_REASON,Unload_Delay_Reason,Pod_Delay_Reason,
        ROUTE_MILE,addtime,addwho,WHSE_ID,load_print_count,Shpm_Print_Count,Assign_Time,belong_jrny_no,first_jrny_no,last_jrny_no,END_UNLOAD_ID,
        END_UNLOAD_NAME,END_UNLOAD_ADDRESS,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,
        LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,
        END_TEL,END_CONTACT,SIGN_ORG_ID_NAME,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,
        BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME)
        select sys_guid(),t_new_no1,ODR_NO,in_shpm_no,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,
        ODR_TIME,t_load_no,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,PRE_LOAD_TIME,
        DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,FROM_UNLOAD_TIME,
        PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,t_status,t_status_name,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,CREATE_ORG_ID,
        EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,COD_FLAG,TRANS_UOM,TOT_QNTY,BIZ_TYP,LOAD_REGION,UNLOAD_REGION,
        TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,
        SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,'Y',SPLIT_REASON_CODE,SPLIT_REASON,
        ASSIGN_STAT,ABNOMAL_NOTES,Losdam_Flag,START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,
        OP_LOAD_TIME,SIGNATARY,Op_Unload_Time,Op_Pod_Time,LOAD_SEQ,Unload_Seq,LOAD_DELAY_REASON,Unload_Delay_Reason,Pod_Delay_Reason,
        ROUTE_MILE,sysdate,in_user_id,WHSE_ID,load_print_count,shpm_print_count,assign_time,belong_jrny_no,first_jrny_no,last_jrny_no,UNLOAD_ID,
        UNLOAD_NAME,UNLOAD_ADDRESS,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,
        LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,
        END_TEL,END_CONTACT,SIGN_ORG_ID_NAME,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,
        BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME 
        from TRANS_SHIPMENT_HEADER WHERE SHPM_NO = in_shpm_no;

        insert into TRANS_SHIPMENT_HEADER(ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,
        ODR_TIME,LOAD_NO,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,PRE_LOAD_TIME,
        DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,FROM_UNLOAD_TIME,
        PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,STATUS,STATUS_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,CREATE_ORG_ID,
        EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,COD_FLAG,TRANS_UOM,TOT_QNTY,BIZ_TYP,LOAD_REGION,UNLOAD_REGION,
        TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,
        SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,SPLIT_FLAG,SPLIT_REASON_CODE,SPLIT_REASON,
        ASSIGN_STAT,ABNOMAL_NOTES,Losdam_Flag,START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,
        OP_LOAD_TIME,SIGNATARY,Op_Unload_Time,Op_Pod_Time,LOAD_SEQ,Unload_Seq,LOAD_DELAY_REASON,Unload_Delay_Reason,Pod_Delay_Reason,
        ROUTE_MILE,addtime,addwho,WHSE_ID,load_print_count,shpm_print_count,assign_time,belong_jrny_no,first_jrny_no,last_jrny_no,END_UNLOAD_ID,
        END_UNLOAD_NAME,END_UNLOAD_ADDRESS,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,
        LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,
        END_TEL,END_CONTACT,SIGN_ORG_ID_NAME,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,
        BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME)
        select sys_guid(),t_new_no2,ODR_NO,in_shpm_no,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,
        ODR_TIME,t_load_no,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,PRE_LOAD_TIME,
        DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,FROM_UNLOAD_TIME,
        PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,t_status,t_status_name,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,CREATE_ORG_ID,
        EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,'N',TRANS_UOM,TOT_QNTY,BIZ_TYP,LOAD_REGION,UNLOAD_REGION,
        TOT_GROSS_W,TOT_VOL,TOT_NET_W,'0',TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,
        SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,'Y',SPLIT_REASON_CODE,SPLIT_REASON,
        ASSIGN_STAT,ABNOMAL_NOTES,Losdam_Flag,START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,
        OP_LOAD_TIME,SIGNATARY,Op_Unload_Time,Op_Pod_Time,LOAD_SEQ,Unload_Seq,LOAD_DELAY_REASON,Unload_Delay_Reason,Pod_Delay_Reason,
        ROUTE_MILE,sysdate,in_user_id,WHSE_ID,load_print_count,shpm_print_count,assign_time,belong_jrny_no,first_jrny_no,last_jrny_no,UNLOAD_ID,
        UNLOAD_NAME,UNLOAD_ADDRESS,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,
        LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,
        END_TEL,END_CONTACT,SIGN_ORG_ID_NAME,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,
        BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME
         from TRANS_SHIPMENT_HEADER WHERE SHPM_NO = in_shpm_no;

        --插入业务日志
        insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
        values(sys_guid(),RDPG_STAT.QNTY_SPIT,RDPG_STAT.SHPM_NO,t_new_no1,'换车拆分,由作业单[' || in_shpm_no || ']拆分生成新作业单',sysdate,in_user_id);
        --插入业务日志
        insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
        values(sys_guid(),RDPG_STAT.QNTY_SPIT,RDPG_STAT.SHPM_NO,t_new_no2,'换车拆分,由作业单[' || in_shpm_no || ']拆分生成新作业单',sysdate,in_user_id);

        for   i   in   1..in_shpm_row.count   loop
            t_cur_row := in_shpm_row(i);
            t_cur_qnty := in_shpm_qnty(i);
            t_cur_vol := in_shpm_vol(i);
            t_cur_gw := in_shpm_gw(i);
            t_cur_nw := in_shpm_nw(i);
            t_cur_worth := in_shpm_worth(i);
            t_other_row := t_other_row || ',' || t_cur_row;
            select QNTY,VOL,G_WGT,N_WGT,WORTH,QNTY_EACH into t_sum_qnty,t_sum_vol,t_sum_gw,t_sum_nw,t_sum_worth,t_sum_each_qnty
              from TRANS_SHIPMENT_ITEM WHERE SHPM_NO = in_shpm_no and SHPM_ROW = t_cur_row;

            t_cur_each_qnty := t_cur_qnty * t_sum_each_qnty / t_sum_qnty;
            --数量有小数的拆分要四舍五入 fanglm
            select round(t_cur_each_qnty,0) into t_cur_each_qnty from dual;
            --插入拆分作业单的第一部分
            insert into TRANS_SHIPMENT_ITEM(ID,SHPM_NO,SHPM_ROW,PARN_SHPM_NO,QNTY,VOL,G_WGT,N_WGT,WORTH,addtime,addwho,
            ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,VOL_UNIT,WGT_UNIT,PRICE,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,
            UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,
            LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,NOTES,ODR_QNTY,UNLOAD_QNTY_EACH,QNTY_EACH,TEMPERATURE1,SKU_CLS,MIX_FLAG)
            select sys_guid(),t_new_no1,t_cur_row,in_shpm_no,t_cur_qnty,t_cur_vol,t_cur_gw,t_cur_nw,t_cur_worth,sysdate,in_user_id,
            ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,VOL_UNIT,WGT_UNIT,PRICE,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,
            UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,
            LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,NOTES,ODR_QNTY,UNLOAD_QNTY_EACH,t_cur_each_qnty,TEMPERATURE1,SKU_CLS,MIX_FLAG
            from TRANS_SHIPMENT_ITEM WHERE SHPM_NO = in_shpm_no and SHPM_ROW = t_cur_row;
            if t_cur_qnty <> t_sum_qnty then
                  --非整条明细做拆分
                begin
                  insert into TRANS_SHIPMENT_ITEM(ID,SHPM_NO,SHPM_ROW,PARN_SHPM_NO,QNTY,VOL,G_WGT,N_WGT,WORTH,addtime,addwho,
                  ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,VOL_UNIT,WGT_UNIT,PRICE,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,
                  UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,
                  LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,NOTES,ODR_QNTY,QNTY_EACH,TEMPERATURE1,SKU_CLS,MIX_FLAG)
                  select sys_guid(),t_new_no2,t_cur_row,in_shpm_no,(QNTY - t_cur_qnty),(VOL - t_cur_vol),
                  (G_WGT - t_cur_gw),(N_WGT - t_cur_nw),(WORTH - t_cur_worth),sysdate,in_user_id,
                  ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,VOL_UNIT,WGT_UNIT,PRICE,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,
                  UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,
                  LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,NOTES,ODR_QNTY,(t_sum_each_qnty - t_cur_each_qnty),TEMPERATURE1,SKU_CLS,MIX_FLAG
                  from TRANS_SHIPMENT_ITEM WHERE SHPM_NO = in_shpm_no and SHPM_ROW = t_cur_row;
                end;
            end if;
        end   loop;

        --将剩余未勾选的作业单明细加入到第二段当中
        t_other_row := substr(t_other_row,2,length(t_other_row) - 1);
        t_sql := '
        insert into TRANS_SHIPMENT_ITEM(ID,SHPM_NO,PARN_SHPM_NO,addtime,addwho,SHPM_ROW,QNTY,VOL,G_WGT,N_WGT,WORTH,
        ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,VOL_UNIT,WGT_UNIT,PRICE,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,
        UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,
        LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,NOTES,ODR_QNTY,UNLOAD_QNTY_EACH,QNTY_EACH,TEMPERATURE1,SKU_CLS,MIX_FLAG)
        select sys_guid(),'''||t_new_no2||''','''||in_shpm_no||''',sysdate,'''||in_user_id||''',SHPM_ROW,QNTY,VOL,G_WGT,N_WGT,WORTH,
        ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,VOL_UNIT,WGT_UNIT,PRICE,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,
        UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,
        LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,NOTES,ODR_QNTY,UNLOAD_QNTY_EACH,QNTY_EACH,TEMPERATURE1,SKU_CLS,MIX_FLAG
        from TRANS_SHIPMENT_ITEM WHERE SHPM_NO = ''' || in_shpm_no|| ''' and SHPM_ROW not in (' || t_other_row || ')';
        --dbms_output.put_line(t_sql);
        execute   immediate t_sql;

        --更新订单头的汇总毛重、体积、净重、货值、最小单位数量
        UPDATE TRANS_SHIPMENT_HEADER SET (TOT_GROSS_W, TOT_NET_W, TOT_VOL, TOT_QNTY,TOT_QNTY_EACH)
        = ( select SUM(G_WGT) as SUM_GROSS_W,SUM(N_WGT) AS SUM_NET_W
          ,SUM(VOL) AS SUM_VOL,SUM(QNTY) AS SUM_QNTY,SUM(QNTY_EACH) AS SUM_QNTY_EACH
          from TRANS_SHIPMENT_ITEM where TRANS_SHIPMENT_HEADER.SHPM_NO = TRANS_SHIPMENT_ITEM.SHPM_NO
          group by SHPM_NO
         ) where SHPM_NO = in_shpm_no || '_S1';

        UPDATE TRANS_SHIPMENT_HEADER SET (TOT_GROSS_W, TOT_NET_W, TOT_VOL, TOT_QNTY,TOT_QNTY_EACH)
        = ( select SUM(G_WGT) as SUM_GROSS_W,SUM(N_WGT) AS SUM_NET_W
          ,SUM(VOL) AS SUM_VOL,SUM(QNTY) AS SUM_QNTY,SUM(QNTY_EACH) AS SUM_QNTY_EACH
          from TRANS_SHIPMENT_ITEM where TRANS_SHIPMENT_HEADER.SHPM_NO = TRANS_SHIPMENT_ITEM.SHPM_NO
          group by SHPM_NO
         ) where SHPM_NO = in_shpm_no || '_S2';

         --插入业务日志
         insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
         values(sys_guid(),RDPG_STAT.QNTY_SPIT,RDPG_STAT.SHPM_NO,in_shpm_no,'作业单[' || in_shpm_no || ']拆分成:' || t_new_no1 || ',' || t_new_no2,sysdate,in_user_id);

         --update trans_bill_rece set doc_no = t_new_no1 where charge_type = '53EB6809BFCC436799F735AAE23658B9' and doc_no = t_odr_no;
        commit;
        EXCEPTION
           WHEN OTHERS THEN
           rollback;
           output_result:='01'+sqlerrm;
        return;
END;

/

--caijiante 2017-5-17
create or replace view v_trans_change_record as
select
 t.id,t.plate_no1,t.vehicle_typ_id1,t.driver1,t.mobile1,t.plate_no2,t.vehicle_typ_id2,t.driver2,
 t.mobile2,t.card_no1,t.card_no2,t.card_no3,t.card_no4,t.change_time,t.change_area_id,t.change_reason,
 t.notes,t.addtime,t.addwho,t.edittime,t.editwho,t.load_no1,t.load_no2,t.suplr_id1,t.suplr_id2,
 b1.vehicle_type as VEHICLE_TYP_ID1_NAME,
 b2.vehicle_type as VEHICLE_TYP_ID2_NAME,
 b3.name_c as CHANGE_REASON_NAME,
 b4.short_name as SUPLR_ID1_NAME,
 b5.short_name as SUPLR_ID2_NAME
from TRANS_CHANGE_RECORD t,BAS_VEHICLE_TYPE b1,BAS_VEHICLE_TYPE b2,BAS_CODES b3,bas_supplier b4,bas_supplier b5
where t.VEHICLE_TYP_ID1=b1.id(+)
and t.VEHICLE_TYP_ID2=b2.id(+)
and t.change_reason=b3.id(+) 
and t.suplr_id1=b4.id(+) 
and t.suplr_id2=b5.id(+);

--yuanlei 2017-05-17
ALTER TABLE BILL_REC_DEDUCT ADD TOTAL_AMOUNT NUMBER(18,8);

CREATE OR REPLACE PROCEDURE SP_DAMAGE_CONFIRM
(
 in_shpm_no IN VARCHAR2,  --作业单号
 in_user_id IN VARCHAR2,
 out_put_result out varchar2
) 
IS 
 op_sum_amount NUMBER(18,8);
 op_company_amount NUMBER(18,8);
 op_driver_amount NUMBER(18,8);
 
 op_odr_no varchar2(100);
 op_custom_odr_no varchar2(100);
 op_load_no varchar2(100);
 op_plate_no varchar2(50);
 op_customer_id varchar2(32);
 op_belong_month varchar2(20);
 op_notes varchar2(255);
 op_vehicle_type varchar2(32);
 op_suplr_id varchar2(32);
 
 t_count number(4);
BEGIN
    out_put_result := '00';
    
    select to_char(add_months(sysdate,-1), 'YYYYMM') into op_belong_month from dual;
    
    begin
        select sum(nvl(amount,0)),sum(nvl(company_acount,0)),sum(nvl(driver_acount,0)),max(notes)
        into op_sum_amount,op_company_amount,op_driver_amount,op_notes
        from trans_loss_damage where shpm_no = in_shpm_no;
    exception when no_data_found then
        op_sum_amount := 0;
        op_company_amount := 0;
        op_driver_amount := 0;
    end;
     
    if op_sum_amount <= 0 then
        out_put_result :='01无货损信息或货损金额小于0!';
        return; 
    end if;
    select odr_no,customer_id,custom_odr_no,load_no,plate_no,vehicle_typ_id,suplr_id
    into op_odr_no,op_customer_id,op_custom_odr_no,op_load_no,op_plate_no,op_vehicle_type,op_suplr_id
    from trans_shipment_header where shpm_no = in_shpm_no;
    
    select count(1) into t_count from bill_rec_deduct where shpm_no = in_shpm_no and deduct_type = '8B187C3543164EA5A5DE5640B77D23FE';
    if t_count > 0 then
        out_put_result :='01已存在相同作业单的货损货差扣款!';
        return; 
    end if;
    
    insert into bill_rec_deduct(id,customer_id,belong_month,deduct_type,deduct_amount,load_no,plate_no
      ,shpm_no,custom_odr_no,descr,total_amount,insur_amount,suplr_amount,staff_amount,status,odr_no,init_no,addtime,addwho)
    values(sys_guid(),op_customer_id,op_belong_month,'8B187C3543164EA5A5DE5640B77D23FE',op_sum_amount,op_company_amount,op_load_no,op_plate_no
      ,in_shpm_no,op_custom_odr_no,op_notes,0,op_driver_amount,0,'0B442D1F9B044E73AB891EBA65E28576',op_odr_no,'X',sysdate,in_user_id);
    
    insert into bill_pay_deduct(id,suplr_id,belong_month,deduct_type,deduct_amount,load_no,plate_no
      ,shpm_no,custom_odr_no,descr,status,vehicle_typ_id,odr_no,init_no,addtime,addwho)
    values(sys_guid(),op_suplr_id,op_belong_month,'8B187C3543164EA5A5DE5640B77D23FE',op_driver_amount,op_load_no,op_plate_no
      ,in_shpm_no,op_custom_odr_no,op_notes,'0B442D1F9B044E73AB891EBA65E28576',op_vehicle_type,op_odr_no,'X',sysdate,in_user_id);
    
    commit;

Exception
     WHEN OTHERS THEN
         out_put_result :='01'||sqlcode || sqlerrm; --失败标记
         ROLLBACK;
END;
/

--yuanlei 2017-05-17 sys_idseq
11?	DAMAGE_NO	1	20170517	

--yuanlei 2017-05-17 sys_function
2?	P09_F045	货损赔偿单	DAMAGE BILL	B	Y	P06_T05	0,P_F07,P_B01	wpsadmin	4	9067	2016/12/21 9:36:20	com.rd.client.view.settlement.RecDamageView	20557		
3?	P09_F046	待审货损赔偿单	DAMAGE BILL	B	Y	P06_T05	0,P_F07,P_B01	wpsadmin	4	9068	2016/12/21 9:36:20	com.rd.client.view.settlement.RecAuditDamageView	20558		

--lml 2017-05-17 sys_func_page
22981	P09_F045_P0	P09_F045	按钮	150	Y	27-5月 -15	wpsadmin	按钮	B
22982	P09_F045_P0_01	P09_F045_P0	提交确认	150	Y	27-5月 -15	wpsadmin	提交确认	B
22983	P09_F045_P0_02	P09_F045_P0	取消提交	150	Y	27-5月 -15	wpsadmin	取消提交	B

--yuanlei 2017-05-17
-- Create table
create table BILL_REC_DAMAGE
(
  ID             VARCHAR2(32) not null,
  CUSTOMER_ID    VARCHAR2(32),
  BELONG_MONTH   VARCHAR2(50),
  TOTAL_AMOUNT   NUMBER(18,8),
  DESCR          VARCHAR2(600),
  INSUR_AMOUNT   NUMBER(18,8),
  SUPLR_AMOUNT   NUMBER(18,8),
  STAFF_AMOUNT   NUMBER(18,8),
  COMPANY_AMOUNT NUMBER(18,8),
  ADDTIME        DATE not null,
  ADDWHO         VARCHAR2(20) not null,
  EDITTIME       DATE,
  EDITWHO        VARCHAR2(20),
  STATUS         VARCHAR2(32),
  ROLE_ID        VARCHAR2(32),
  LISTER         VARCHAR2(50),
  LISTER_TIME    DATE,
  DAMAGE_NO      VARCHAR2(100),
  DEDUCT_ID      VARCHAR2(100)
)
-- Add comments to the columns 
comment on column BILL_REC_DAMAGE.CUSTOMER_ID
  is '客户';
comment on column BILL_REC_DAMAGE.BELONG_MONTH
  is '所属期';
comment on column BILL_REC_DAMAGE.TOTAL_AMOUNT
  is '赔偿总金额';
comment on column BILL_REC_DAMAGE.DESCR
  is '货损货差情况说明';
comment on column BILL_REC_DAMAGE.INSUR_AMOUNT
  is '保险公司承担金额';
comment on column BILL_REC_DAMAGE.SUPLR_AMOUNT
  is '承运商/司机承担金额';
comment on column BILL_REC_DAMAGE.STAFF_AMOUNT
  is '本公司员工承担金额';
comment on column BILL_REC_DAMAGE.COMPANY_AMOUNT
  is '本公司承担金额';
comment on column BILL_REC_DAMAGE.STATUS
  is '状态 CODEID  = ''APPROVE_STS''';
comment on column BILL_REC_DAMAGE.ROLE_ID
  is '当前审批角色ID = SYS_ROLE.ID''';
comment on column BILL_REC_DAMAGE.LISTER
  is '送审人';
comment on column BILL_REC_DAMAGE.LISTER_TIME
  is '送审时间';
comment on column BILL_REC_DAMAGE.DAMAGE_NO
  is '货损赔偿单号';
comment on column BILL_REC_DAMAGE.DEDUCT_ID
  is '扣款单号';

CREATE OR REPLACE PROCEDURE BMS_REC_DAMAGE_COMMIT
(
 in_damage_no IN VARCHAR2,
 in_user_id IN VARCHAR2,
 out_put_result out varchar2
) 
IS 
 op_role_id varchar(32);
 t_show_seq number;
 t_status varchar(50);
 t_customer_id varchar2(32);
 
 t_id varchar2(32);
BEGIN
    out_put_result := '00';
    select status,customer_id into t_status,t_customer_id from BILL_REC_DAMAGE where DAMAGE_NO=in_damage_no;
    if t_status <> '10' and t_status <> '15' then
        out_put_result :='01当前赔偿单状态不需要提交确认，请刷新赔偿单';
        return;
    end if;
    
    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'DAMAGE_NO' and t1.customer_id = t_customer_id;
        select MIN(show_seq) into t_show_seq from sys_approve_set where HEAD_ID = t_id;
        select ROLE_ID into op_role_id from sys_approve_set where HEAD_ID = t_id and SHOW_SEQ=t_show_seq;
        update  BILL_REC_DAMAGE set ROLE_ID=op_role_id,status = '20',LISTER=in_user_id,LISTER_TIME=sysdate  where DAMAGE_NO=in_damage_no;
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)
            values(SYS_GUID(),in_damage_no,'DAMAGE_NO',op_role_id,in_user_id,sysdate,'已申请','申请审批',sysdate,in_user_id);
        commit;
    exception when no_data_found then
            out_put_result :='01赔偿单未设置对应的审批流程!';
            return;
    end;

Exception
     WHEN OTHERS THEN
         out_put_result :='01'||sqlcode || sqlerrm; --失败标记
         ROLLBACK;
END;

alter table sys_approve_log add DOC_TYPE varchar2(40) null;

CREATE OR REPLACE PROCEDURE BMS_REC_DAMAGE_CANCEL
(
 in_damage_no IN VARCHAR2
,out_put_result out varchar2
) IS
t_rec_bill_status varchar(50);
t_app_count number;
BEGIN
      out_put_result := '00';
      select status into t_rec_bill_status from BILL_REC_DAMAGE where DAMAGE_NO=in_damage_no;
      if t_rec_bill_status='20' then
          select count(1) into t_app_count from SYS_APPROVE_LOG where DOC_TYPE = 'DAMAGE_NO' and DOC_NO = in_damage_no and APPROVER_RESULT in ('已审批','已打回');
          if t_app_count=0 then
              delete  from SYS_APPROVE_LOG where DOC_TYPE = 'DAMAGE_NO' and DOC_NO=in_damage_no and APPROVER_RESULT ='已申请';
              update BILL_REC_DAMAGE set status='10' where DAMAGE_NO=in_damage_no;
          else
              out_put_result :='当前货损赔偿单审批中，不允许取消确认';
          end if;
      else
          out_put_result :='当前货损赔偿单状态不需要取消确认，请刷新货损赔偿单';
      end if;
      commit;
Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

--caijiante 2017-05-17 
CREATE OR REPLACE VIEW V_REC_DEDUCT AS
SELECT
    r.addtime,r.addwho,r.adjust_reason,r.belong_month,r.custom_odr_no,r.deduct_amount,r.deduct_type,r.odr_no,r.total_amount,
    r.descr,r.edittime,r.editwho,r.id,r.insur_amount,r.load_no,r.plate_no,r.shpm_no,r.staff_amount,r.status,r.suplr_amount,r.customer_id,
    b1.name_c as DEDUCT_TYPE_NAME,b2.name_c as ADJUST_REASON_NAME,b3.name_c as STATUS_NAME,c.customer_cname as CUSTOMER_ID_NAME,r.init_no
FROM BILL_REC_DEDUCT r,BAS_CODES b1,BAS_CODES b2,BAS_CODES b3,BAS_CUSTOMER c
where r.deduct_type=b1.id(+)
and r.adjust_reason=b2.id(+)
and r.status=b3.id(+)
and r.customer_id=c.id(+);

--yuanlei 2017-05-17
update BAS_CODES SET CODE = 'DAMAGE_NO' where prop_code = 'APPROVE_DOC' and CODE = 'CLAIM';
drop Procedure BMS_ADJNO_COMMIT;
drop Procedure BMS_ADJNO_CANCEL;
drop Procedure BMS_INVOICE_COMMIT;
drop Procedure BMS_INVOICE_CANCEL;
drop Procedure PAY_BILL_CONFIRM;
drop Procedure PAY_BILL_CANCEL;
drop Procedure PAY_REQ_CONFIRM;
drop Procedure PAY_REQ_CANCEL;
drop Procedure BMS_ADJNO_AUDIT_AGREE;
drop Procedure BMS_ADJNO_AUDIT_BACK;
drop Procedure INVOICE_CHECK;
drop Procedure INVOICE_CHECK_BACK;
drop Procedure REQ_CHECK;
drop Procedure REQ_CHECK_BACK;
drop Procedure BMS_CREATE_ADJNO;
alter table BILL_PAY_INITIAL add CUSTOMER_ID VARCHAR2(32) NULL;
alter table BILL_PAY_INITDETAILS add CUSTOMER_ID VARCHAR2(32) NULL;
alter table BILL_PAY_REQUEST add CUSTOMER_ID  VARCHAR2(32) NULL;
alter table bill_pay_adjust add CUSTOMER_ID VARCHAR2(32) NULL;

CREATE OR REPLACE PROCEDURE BMS_REC_ADJNO_COMMIT
(
 in_adj_no IN VARCHAR2,
 in_user_id IN VARCHAR2,
 out_put_result out varchar2
) 
IS 
 op_role_id varchar(32);
 t_show_seq number;
 t_status varchar(50);
 t_customer_id varchar2(32);
 t_id varchar2(32);
BEGIN
    out_put_result := '00';
    select status,buss_id into t_status,t_customer_id from BILL_REC_ADJUST where ADJ_NO=in_adj_no;
    if t_status <> '10' and t_status <> '15' then
        out_put_result :='01当前调整单状态不需要提交确认，请刷新调整单';
        return;
    end if;
    
    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_ADJNO' and t1.customer_id = t_customer_id;
        select MIN(show_seq) into t_show_seq from sys_approve_set where HEAD_ID = t_id;
        --select MIN(show_seq) into t_show_seq from sys_approve_set where DOC_NO ='REC_ADJNO' ;
        select ROLE_ID into op_role_id from sys_approve_set where HEAD_ID = t_id and SHOW_SEQ=t_show_seq;
        update  BILL_REC_ADJUST set ROLE_ID=op_role_id,status = '20',LISTER=in_user_id,LISTER_TIME=sysdate  where ADJ_NO=in_adj_no;
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)
            values(SYS_GUID(),in_adj_no,'REC_ADJNO',op_role_id,in_user_id,sysdate,'已申请','申请审批',sysdate,in_user_id);
        commit;
    exception when no_data_found then
            out_put_result :='01调整单未设置对应的审批流程!';
            return;
    end;

Exception
     WHEN OTHERS THEN
         out_put_result :='01'||sqlcode || sqlerrm; --失败标记
         ROLLBACK;
END;
/

CREATE OR REPLACE PROCEDURE BMS_REC_ADJNO_CANCEL
(
 in_adj_no IN VARCHAR2
,out_put_result out varchar2
) IS
t_rec_bill_status varchar(50);
t_app_count number;
BEGIN
      out_put_result := '00';
      select status into t_rec_bill_status from BILL_REC_ADJUST where ADJ_NO=in_adj_no;
      if t_rec_bill_status='20' then
          select count(1) into t_app_count from SYS_APPROVE_LOG where DOC_TYPE = 'REC_ADJNO' AND DOC_NO = in_adj_no and APPROVER_RESULT in ('已审批','已打回');
          if t_app_count=0 then
              delete  from SYS_APPROVE_LOG where DOC_TYPE = 'DAMAGE_NO' AND DOC_NO=in_adj_no and APPROVER_RESULT ='已申请';
              update BILL_REC_ADJUST set status='10' where ADJ_NO=in_adj_no;
          else
              out_put_result :='当前调整单审批中，不允许取消确认';
          end if;
      else
          out_put_result :='当前调整单状态不需要取消确认，请刷新调整单';
      end if;
      commit;
Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_REC_ADJNO_CANCEL
(
 in_adj_no IN VARCHAR2
,out_put_result out varchar2
) IS
t_rec_bill_status varchar(50);
t_app_count number;
BEGIN
      out_put_result := '00';
      select status into t_rec_bill_status from BILL_REC_ADJUST where ADJ_NO=in_adj_no;
      if t_rec_bill_status='20' then
          select count(1) into t_app_count from SYS_APPROVE_LOG where DOC_TYPE = 'REC_ADJNO' AND DOC_NO = in_adj_no and APPROVER_RESULT in ('已审批','已打回');
          if t_app_count=0 then
              delete  from SYS_APPROVE_LOG where DOC_TYPE = 'REC_ADJNO' AND DOC_NO=in_adj_no and APPROVER_RESULT ='已申请';
              update BILL_REC_ADJUST set status='10' where ADJ_NO=in_adj_no;
          else
              out_put_result :='当前调整单审批中，不允许取消确认';
          end if;
      else
          out_put_result :='当前调整单状态不需要取消确认，请刷新调整单';
      end if;
      commit;
Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_REC_INVOICE_COMMIT
(
in_INVOICE_NO VARCHAR2,   --期初单号
in_user_id varchar2,
output_result out varchar2
)
IS

op_role_id varchar2(32);
t_show_seq number(4);
t_status varchar2(40);
t_amount number(18,8);
t_sum_amount number(18,8);
t_customer_id varchar2(32);
t_id varchar2(32);
begin
    --in_INIT_NO.Extend;
    --in_INIT_NO(1) := '2017032700005';
    output_result := '00';
    select status,ACT_AMOUNT,buss_id into t_status,t_amount,t_customer_id from BILL_REC_INVOICE where INVOICE_NO=IN_INVOICE_NO;
    select sum(ACT_AMOUNT) into t_sum_amount from BILL_REC_INVOICEINFO where INVOICE_NO = IN_INVOICE_NO;
    if t_status <> '10' and t_status <> '15' then
        output_result :='01当前申请单状态不需要申请开票，请刷新申请单';
        return;
    end if;
    if t_sum_amount < t_amount then
        output_result :='01发票总金额必须等于应收金额';
        return;
    end if;
    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_INVOICENO' and t1.customer_id = t_customer_id;
        select MIN(show_seq) into t_show_seq from sys_approve_set where HEAD_ID = t_id;
        --select min(show_seq) into t_show_seq from SYS_APPROVE_SET where doc_no = 'REC_INVOICENO';
        select ROLE_ID into op_role_id from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ=t_show_seq;
        update  BILL_REC_INVOICE set ROLE_ID=op_role_id,status = '20',LISTER=in_user_id,LISTER_TIME=sysdate  where INVOICE_NO=IN_INVOICE_NO;
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)
            values(SYS_GUID(),in_INVOICE_NO,'REC_INVOICENO',op_role_id,in_user_id,sysdate,'已申请','申请审批',sysdate,in_user_id);

    exception when no_data_found then
        output_result :='01开票申请单未设置审批流程，请先设置审批流程!';
        return;
    end;
    commit;
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result := '01' || sqlerrm;
     return;
end;

/

CREATE OR REPLACE PROCEDURE BMS_REC_INVOICE_CANCEL
(
 IN_INVOICE_NO IN VARCHAR2
,out_put_result out varchar2
) IS
t_rec_bill_status varchar(50);
t_app_count number;
BEGIN
      out_put_result := '00';
      select status into t_rec_bill_status from BILL_REC_INVOICE where INVOICE_NO=IN_INVOICE_NO;
      if t_rec_bill_status='20' then
          select count(1) into t_app_count from SYS_APPROVE_LOG where DOC_TYPE = 'REC_ADJNO' and DOC_NO = IN_INVOICE_NO and APPROVER_RESULT in ('已审批','已打回');
          if t_app_count=0 then
              delete  from SYS_APPROVE_LOG where DOC_TYPE = 'REC_ADJNO' and DOC_NO = IN_INVOICE_NO and APPROVER_RESULT ='已申请';
              update BILL_REC_INVOICE set status='10' where INVOICE_NO=IN_INVOICE_NO;
          else
              out_put_result :='当前开票申请单审批中，不允许取消申请';
          end if;
      else
          out_put_result :='当前开票申请单状态不需要取消确认，请刷新开票申请单';
      end if;
      commit;
Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_CREATE_ADJNO
(
in_INIT_NO LST,   --期初单号
in_user_id varchar2,
output_result out varchar2
)
IS
/**
 * 生成调整账单
 */
r_init_no varchar2(100);
r_adj_no varchar2(100);

op_buss_id varchar2(32);
op_buss_name varchar2(100);
op_belong_month varchar2(50);
op_initital_amount NUMBER(18,8);
op_init_amount number(18,8);
op_tax_amount NUMBER(18,8);
op_subtax_amount number(18,8);
op_adj_amount number(18,8);
op_confirm_amount number(18,8);
op_account_stat varchar2(32);
op_role_id varchar2(32);
op_tax number(4,2);
op_hold_flag char(1);
op_customer_id varchar2(32);

t_count number(4);

--in_INIT_NO LST := LST();
begin
    --in_INIT_NO.Extend;
    --in_INIT_NO(1) := '2017032700005';
    output_result := '00';
    for   i   in   1..in_INIT_NO.count loop
        r_init_no := in_INIT_NO(i);
        select suplr_id,suplr_name,belong_month,initital_amount,adj_amount,confirm_amount,tax_amount,subtax_amount,account_stat,init_amount,hold_flag,customer_id
        into op_buss_id,op_buss_name,op_belong_month,op_initital_amount,op_adj_amount,op_confirm_amount,op_tax_amount,op_subtax_amount,op_account_stat,op_init_amount,op_hold_flag,op_customer_id
            from bill_pay_initial WHERE INIT_NO = r_init_no;
        if op_account_stat = '20' then
            output_result :='01对账单已确认无法生成调整单!';
            return;
        end if;
        if op_hold_flag = 'Y' then
            output_result :='01对账单已生成调整单,不能重复生成!';
            return;
        end if;
        if op_adj_amount = 0 then
            output_result :='01金额未调整,不需要生成调整账单!';
            return;
        end if;
        select udf2 into op_tax from bas_supplier where id = op_buss_id;
        if op_tax is null then
            op_tax := 0;
        end if;
        SP_GET_IDSEQ('PAY_ADJNO',r_adj_no);

        select count(1) into t_count from BILL_PAY_ADJUST where init_no = r_init_no;

        op_tax_amount := round(op_adj_amount * op_tax /(100+op_tax),2);
        insert into BILL_PAY_ADJUST(id,adj_no,suplr_id,suplr_name,belong_month,INITITAL_AMOUNT,CONFIRM_AMOUNT,ADJ_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,status,role_id,lister,lister_time,addtime,addwho,init_no,customer_id)
        values(sys_guid(),r_adj_no,op_buss_id,op_buss_name,op_belong_month,op_initital_amount,op_confirm_amount,op_adj_amount,op_tax_amount,op_adj_amount-op_tax_amount,'10',op_role_id,in_user_id,sysdate,sysdate,in_user_id,r_init_no,op_customer_id);

        if t_count = 0 then
            insert into BILL_PAY_ADJDETAILS(ID,ADJ_NO,suplr_Id,suplr_name,LOAD_NO,PLATE_NO,VEHICLE_TYP_ID,notes,init_amount,initital_amount,confirm_amount1,adj_amount1,addwho,addtime,init_no,init_detail_id)
            select sys_guid(),r_adj_no,suplr_id,suplr_name,load_no,plate_no,vehicle_typ_id,notes,init_amount,tot_amount,confirm_amount,adj_amount,in_user_id,sysdate,r_init_no,id from BILL_PAY_INITDETAILS
            where init_no = r_init_no;
        elsif t_count = 1 then
            update BILL_PAY_ADJDETAILS set (INITITAL_AMOUNT,Confirm_amount2,ADJ_AMOUNT2) = (select tot_amount,Confirm_amount,adj_amount from BILL_PAY_INITDETAILS where BILL_PAY_ADJDETAILS.Init_Detail_Id = BILL_PAY_INITDETAILS.ID);
        elsif t_count = 2 then
            update BILL_PAY_ADJDETAILS set (INITITAL_AMOUNT,Confirm_amount3,ADJ_AMOUNT3) = (select tot_amount,Confirm_amount,adj_amount from BILL_PAY_INITDETAILS where BILL_PAY_ADJDETAILS.Init_Detail_Id = BILL_PAY_INITDETAILS.ID);
        else
            output_result :='01最多只允许调整3次账单!';
            return;
        end if;
        update bill_pay_initial set HOLD_FLAG = 'Y' where INIT_NO = r_init_no;
    end loop;
    commit;
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result := '01' || sqlerrm;
     return;
end;

/

CREATE OR REPLACE PROCEDURE BMS_REC_CREATE_ADJNO
(
in_INIT_NO LST,   --期初单号
in_user_id varchar2,
output_result out varchar2
)
IS
/**
 * 生成调整账单
 */
r_init_no varchar2(100);
r_adj_no varchar2(100);


op_buss_id varchar2(32);
op_buss_name varchar2(100);
op_belong_month varchar2(50);
op_initital_amount NUMBER(18,8);
op_init_amount number(18,8);
op_tax_amount NUMBER(18,8);
op_subtax_amount number(18,8);
op_adj_amount number(18,8);
op_confirm_amount number(18,8);
op_account_stat varchar2(32);
op_role_id varchar2(32);
op_tax number(4,2);
op_hold_flag char(1);

t_count number(4);

--in_INIT_NO LST := LST();
begin
    --in_INIT_NO.Extend;
    --in_INIT_NO(1) := '2017032700005';
    output_result := '00';
    for   i   in   1..in_INIT_NO.count loop
        r_init_no := in_INIT_NO(i);
        select buss_id,buss_name,belong_month,initital_amount,adj_amount,confirm_amount,tax_amount,subtax_amount,account_stat,init_amount,hold_flag 
        into op_buss_id,op_buss_name,op_belong_month,op_initital_amount,op_adj_amount,op_confirm_amount,op_tax_amount,op_subtax_amount,op_account_stat,op_init_amount,op_hold_flag
            from bill_rec_initial WHERE INIT_NO = r_init_no;
        if op_account_stat = '20' then
            output_result :='01对账单已确认无法生成调整单!';
            return;
        end if;
        if op_hold_flag = 'Y' then
            output_result :='01对账单已生成调整单,不能重复生成!';
            return; 
        end if;
        if op_adj_amount = 0 then
            output_result :='01金额未调整,不需要生成调整账单!';
            return; 
        end if;
        select udf2 into op_tax from bas_customer where id = op_buss_id;
        if op_tax is null then
            op_tax := 0;
        end if;
        SP_GET_IDSEQ('RECE_ADJNO',r_adj_no);
        
        select count(1) into t_count from BILL_REC_ADJUST where init_no = r_init_no;
        
        op_tax_amount := round(op_adj_amount * op_tax /(100+op_tax),2);
        insert into BILL_REC_ADJUST(id,adj_no,buss_id,buss_name,belong_month,INITITAL_AMOUNT,CONFIRM_AMOUNT,ADJ_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,status,role_id,lister,lister_time,addtime,addwho,init_no)
        values(sys_guid(),r_adj_no,op_buss_id,op_buss_name,op_belong_month,op_initital_amount,op_confirm_amount,op_adj_amount,op_tax_amount,op_adj_amount-op_tax_amount,'10',op_role_id,in_user_id,sysdate,sysdate,in_user_id,r_init_no);
        
        if t_count = 0 then
            insert into BILL_REC_ADJDETAILS(ID,ADJ_NO,Buss_Id,buss_name,Odr_No,Custom_Odr_No,vehicle_typ_id,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,Notes,init_amount,initital_amount,confirm_amount1,adj_amount1,addwho,addtime,init_no,init_detail_id)
            select sys_guid(),r_adj_no,buss_id,buss_name,odr_no,custom_odr_no,vehicle_typ_id,load_date,unload_date,load_name,unload_name,notes,init_amount,TOT_AMOUNT,confirm_amount,adj_amount,in_user_id,sysdate,r_init_no,id from BILL_REC_INITDETAILS
            where init_no = r_init_no;
        elsif t_count = 1 then
            update BILL_REC_ADJDETAILS set (INITITAL_AMOUNT,Confirm_amount2,ADJ_AMOUNT2) = (select tot_amount,Confirm_amount,adj_amount from BILL_REC_INITDETAILS where BILL_REC_ADJDETAILS.Init_Detail_Id = BILL_REC_INITDETAILS.ID);
        elsif t_count = 2 then
            update BILL_REC_ADJDETAILS set (INITITAL_AMOUNT,Confirm_amount3,ADJ_AMOUNT3) = (select tot_amount,Confirm_amount,adj_amount from BILL_REC_INITDETAILS where BILL_REC_ADJDETAILS.Init_Detail_Id = BILL_REC_INITDETAILS.ID);
        else
            output_result :='01最多只允许调整3次账单!';
            return;
        end if;
        update bill_rec_initial set HOLD_FLAG = 'Y' where INIT_NO = r_init_no;
    end loop;
    commit;
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result := '01' || sqlerrm;
     return;
end;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_ADJNO_CONFIRM
(
 in_adj_no IN VARCHAR2,
 in_user_id IN VARCHAR2,
 out_put_result out varchar2
)
IS
 op_role_id varchar(32);
 t_show_seq number;
 t_status varchar(50);
 t_customer_id varchar2(32);
 t_id varchar2(32);
BEGIN
    out_put_result := '00';
    select status,customer_id into t_status,t_customer_id from BILL_PAY_ADJUST where ADJ_NO=in_adj_no;
    if t_status <> '10' and t_status <> '15' then
        out_put_result :='01当前调整单状态不需要提交确认，请刷新调整单';
        return;
    end if;

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'PAY_ADJNO' and t1.customer_id = t_customer_id;
        select MIN(show_seq) into t_show_seq from sys_approve_set where HEAD_ID = t_id;
        select ROLE_ID into op_role_id from sys_approve_set where HEAD_ID = t_id and SHOW_SEQ=t_show_seq;
        
        update  BILL_PAY_ADJUST set ROLE_ID=op_role_id,status = '20',LISTER=in_user_id,LISTER_TIME=sysdate  where ADJ_NO=in_adj_no;
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)
            values(SYS_GUID(),in_adj_no,op_role_id,'PAY_ADJNO',in_user_id,sysdate,'已申请','申请审批',sysdate,in_user_id);
        commit;
    exception when no_data_found then
            out_put_result :='01调整单未设置对应的审批流程!';
            return;
    end;

Exception
     WHEN OTHERS THEN
         out_put_result :='01'||sqlcode || sqlerrm; --失败标记
         ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_ADJNO_CANCEL
(
 in_adj_no IN VARCHAR2
,out_put_result out varchar2
)
IS
t_pay_bill_status varchar(50);
t_app_count number;
BEGIN

      out_put_result := '00';
      select status into t_pay_bill_status from BILL_PAY_ADJUST where ADJ_NO=in_adj_no;
      if t_pay_bill_status='20' then
          select count(1) into t_app_count from SYS_APPROVE_LOG where DOC_TYPE = 'PAY_ADJNO' AND DOC_NO = in_adj_no and APPROVER_RESULT in ('已审批','已打回');
          if t_app_count=0 then
              delete  from SYS_APPROVE_LOG where DOC_TYPE = 'PAY_ADJNO' and DOC_NO=in_adj_no and APPROVER_RESULT ='已申请';
              update BILL_PAY_ADJUST set status='10' where ADJ_NO=in_adj_no;
          else
              out_put_result :='当前调整单审批中，不允许取消确认';
          end if;
      else
          out_put_result :='当前调整单状态不需要取消确认，请刷新调整单';
      end if;
      commit;
Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

--caijinte 2017-05-18 sys_func_page
983?	22984	P09_F046_P0	P09_F046	按钮功能	150	Y	2015/5/27	wpsadmin	按钮功能	B	
984?	22985	P09_F046_P0_01	P09_F046_P0	审核	150	Y	2015/5/27	wpsadmin	审核	B	

CREATE OR REPLACE PROCEDURE BMS_CONFIRM_REQUEST
(
in_ID LST,   --期初单号
in_user_id varchar2,
output_result out varchar2
)
IS

r_id varchar2(32);
r_request_no varchar2(100);

op_buss_id varchar2(32);
op_buss_name varchar2(100);
op_belong_month varchar2(50);
op_initital_amount NUMBER(18,8);
op_tax_amount NUMBER(18,8);
op_subtax_amount number(18,8);
op_account_stat varchar2(32);
op_role_id varchar2(32);
op_sum_amount number(18,8);
--in_ID LST := LST();
op_init_no varchar2(100);
op_tax number(4,2);
op_customer_id varchar2(32);
begin
    --in_ID.Extend;
    --in_ID(1) := '28CEB1D3CAAD4A38BDF4D3D9858D757A';
    output_result := '00';
    op_sum_amount := 0;
    SP_GET_IDSEQ('PAY_REQNO',r_request_no);
    for   i   in   1..in_ID.count   loop
        r_id := in_ID(i);
        select init_no,suplr_id,suplr_name,belong_month,tot_amount,account_stat,customer_id
        into op_init_no,op_buss_id,op_buss_name,op_belong_month,op_initital_amount,op_account_stat,op_customer_id
            from bill_pay_initdetails WHERE ID = r_id;
        if op_account_stat <> '20' then
            output_result :='01只有已对账的对账单才能生成请款单!';
            return;
        end if;

        op_sum_amount := op_sum_amount + op_initital_amount;
        
        select nvl(udf2,0) into op_tax from bas_supplier where id = op_buss_id;
        
        insert into BILL_PAY_REQDETAILS(ID,REQ_NO,SUPLR_Id,SUPLR_NAME,LOAD_NO,PLATE_NO,VEHICLE_TYP_ID,DRIVER,MOBILE,LOAD_NAME,UNLOAD_NAME,LOAD_DATE,UNLOAD_DATE,Notes,ACT_FEE,TAX_FEE,SUBTAX_FEE)
        select sys_guid(),r_request_no,suplr_id,suplr_name,load_no,plate_no,VEHICLE_TYP_ID,driver,mobile,load_name,unload_name,load_date,unload_date,notes,TOT_AMOUNT,Tax_Amount,Subtax_Amount from BILL_PAY_INITDETAILS
        where id = r_id;
        
        update bill_pay_initdetails set INVOICE_STAT = '20' where id = r_id;
        
        update bill_pay_initial set PAY_FLAG = 'Y' where INIT_NO = op_init_no;
        
    end loop;
    
    op_tax_amount := round(op_sum_amount*op_tax/(100+op_tax),2); 
    op_subtax_amount := op_sum_amount - op_tax_amount;
    
    insert into BILL_PAY_REQUEST(id,REQ_no,suplr_id,suplr_name,belong_month,act_amount,tax_amount,subtax_amount,status,bill_status,pay_status,role_id,lister,lister_time,addtime,addwho,init_no,CUSTOMER_ID)
    values(sys_guid(),r_request_no,op_buss_id,op_buss_name,op_belong_month,op_sum_amount,op_tax_amount,op_subtax_amount,'10','10','10',op_role_id,in_user_id,sysdate,sysdate,in_user_id,op_init_no,op_customer_id);

    commit;
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result := '01' || sqlerrm;
     return;
end;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_REQUEST_CONFIRM
(
 in_req_no IN VARCHAR2,
 in_role_id IN VARCHAR2,
 in_user_id IN VARCHAR2,
 out_put_result out varchar2
)
IS
 op_role_id varchar2(32);
 t_show_seq number;
 t_status varchar(50);
 t_customer_id varchar2(32);
 t_id varchar2(32);
BEGIN
    out_put_result := '00';
    select status,customer_id into t_status,t_customer_id from BILL_PAY_REQUEST where REQ_NO=in_req_no;
    if t_status <> '10' and t_status <> '15' then
        out_put_result :='01当前请款单状态不需要提交确认，请刷新请款单';
        return;
    end if;

    begin
    
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'PAY_REQNO' and t1.customer_id = t_customer_id;
        select MIN(show_seq) into t_show_seq from sys_approve_set where HEAD_ID = t_id;
        select ROLE_ID into op_role_id from sys_approve_set where HEAD_ID = t_id and SHOW_SEQ=t_show_seq;
        
        update  BILL_PAY_REQUEST set ROLE_ID=op_role_id,status = '20',LISTER=in_user_id,LISTER_TIME=sysdate  where REQ_NO=in_req_no;
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)
            values(SYS_GUID(),in_req_no,'PAY_REQNO',in_role_id,in_user_id,sysdate,'已申请','申请审批',sysdate,in_user_id);
        commit;
    exception when no_data_found then
            out_put_result :='01请款单未设置对应的审批流程!';
            return;
    end;

Exception
     WHEN OTHERS THEN
         out_put_result :='01'||sqlcode || sqlerrm; --失败标记
         ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_REQUEST_CANCEL
(
 in_req_no IN VARCHAR2
,out_put_result out varchar2
) IS
t_bill_status varchar2(50);
t_count number;
t_customer_id varchar2(32);
BEGIN
      out_put_result := '00';
      select status,customer_id into t_bill_status,t_customer_id from BILL_PAY_REQUEST where REQ_NO=in_req_no;
      if t_bill_status='20' then
          select count(1) into t_count from SYS_APPROVE_LOG where DOC_TYPE = 'PAY_REQNO' and DOC_NO = in_req_no and APPROVER_RESULT in ('已审批','已打回');
          if t_count=0 then
              delete  from SYS_APPROVE_LOG where DOC_TYPE = 'PAY_REQNO' and DOC_NO=in_req_no and APPROVER_RESULT ='已申请';
              update BILL_PAY_REQUEST set status='10' where REQ_NO=in_req_no;
          else
              out_put_result :='当前请款单审批中，不允许取消确认';
          end if;
      else
          out_put_result :='当前请款单状态不需要取消确认，请刷新请款单';
      end if;
      commit;
Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_DAMAGE_VERIFICATED
(
  in_damage_no IN VARCHAR2
, in_invoice_num IN VARCHAR2
, in_voucher_no IN VARCHAR2
, in_collection_time IN VARCHAR2
, in_user_id IN VARCHAR2
, in_amount IN NUMBER
,out_put_result out varchar2
)
IS
op_amount number;
op_t_amount number;
t_act_amount number;
t_rece_amount number;
BEGIN
   out_put_result := '00';
   begin
     select nvl(RECE_AMOUNT,0) into op_amount from BILL_REC_DMGINVOICEINFO  where INVOICE_NUM=in_invoice_num;
   exception
       WHEN NO_DATA_FOUND THEN
       op_amount:=0;
   end;
   op_t_amount:=in_amount+op_amount;

   update BILL_REC_DMGINVOICEINFO set RECE_BY=in_user_id,RECE_AMOUNT=op_t_amount,RECE_TIME=sysdate where INVOICE_NUM=in_invoice_num;

   insert into BILL_REC_DMGRECELOG (ADDWHO,ADDTIME,ID,DAMAGE_NO,INVOICE_NUM,RECE_BY,RECE_TIME,RECE_AMOUNT,COLLECTION_TIME,Voucher_No) 
   values (in_user_id,sysdate,sys_guid(),in_damage_no,in_invoice_num,in_user_id,sysdate,in_amount,to_date(in_collection_time,'YYYY-MM-DD'),in_voucher_no);

  begin
   select sum(nvl(ACT_AMOUNT,0)) into t_act_amount from BILL_REC_DMGINVOICEINFO where DAMAGE_NO = in_damage_no;
  exception
      WHEN NO_DATA_FOUND THEN
      t_act_amount:=0;
  end;

  begin
    select sum(nvl(RECE_AMOUNT,0)) into t_rece_amount from BILL_REC_DMGINVOICEINFO where DAMAGE_NO = in_damage_no;
  exception
      WHEN NO_DATA_FOUND THEN
      t_rece_amount:=0;
  end;

 if t_act_amount > t_rece_amount then
    update BILL_REC_DAMAGE set REC_STATUS='15' where DAMAGE_NO=in_damage_no;
 end if;

if t_act_amount = t_rece_amount then
    update BILL_REC_DAMAGE set REC_STATUS='20',RECE_BY= in_user_id,RECE_TIME=sysdate where DAMAGE_NO=in_damage_no;
end if;


commit;

Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;


END;

/

-- Create table
create table BILL_REC_DAMAGE
(
  ID             VARCHAR2(32) not null,
  CUSTOMER_ID    VARCHAR2(32),
  BELONG_MONTH   VARCHAR2(50),
  TOTAL_AMOUNT   NUMBER(18,8),
  DESCR          VARCHAR2(600),
  INSUR_AMOUNT   NUMBER(18,8),
  SUPLR_AMOUNT   NUMBER(18,8),
  STAFF_AMOUNT   NUMBER(18,8),
  COMPANY_AMOUNT NUMBER(18,8),
  ADDTIME        DATE not null,
  ADDWHO         VARCHAR2(20) not null,
  EDITTIME       DATE,
  EDITWHO        VARCHAR2(20),
  STATUS         VARCHAR2(32),
  ROLE_ID        VARCHAR2(32),
  LISTER         VARCHAR2(50),
  LISTER_TIME    DATE,
  DAMAGE_NO      VARCHAR2(100),
  DEDUCT_ID      VARCHAR2(100),
  REC_STATUS     VARCHAR2(32),
  RECE_BY        VARCHAR2(20),
  RECE_TIME      DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column BILL_REC_DAMAGE.CUSTOMER_ID
  is '客户';
comment on column BILL_REC_DAMAGE.BELONG_MONTH
  is '所属期';
comment on column BILL_REC_DAMAGE.TOTAL_AMOUNT
  is '赔偿总金额';
comment on column BILL_REC_DAMAGE.DESCR
  is '货损货差情况说明';
comment on column BILL_REC_DAMAGE.INSUR_AMOUNT
  is '保险公司承担金额';
comment on column BILL_REC_DAMAGE.SUPLR_AMOUNT
  is '承运商/司机承担金额';
comment on column BILL_REC_DAMAGE.STAFF_AMOUNT
  is '本公司员工承担金额';
comment on column BILL_REC_DAMAGE.COMPANY_AMOUNT
  is '本公司承担金额';
comment on column BILL_REC_DAMAGE.STATUS
  is '状态 CODEID  = ''APPROVE_STS''';
comment on column BILL_REC_DAMAGE.ROLE_ID
  is '当前审批角色ID = SYS_ROLE.ID''';
comment on column BILL_REC_DAMAGE.LISTER
  is '送审人';
comment on column BILL_REC_DAMAGE.LISTER_TIME
  is '送审时间';
comment on column BILL_REC_DAMAGE.DAMAGE_NO
  is '货损赔偿单号';
comment on column BILL_REC_DAMAGE.DEDUCT_ID
  is '扣款单号';
comment on column BILL_REC_DAMAGE.REC_STATUS
  is '核销状态CODEID = ''RECE_STAT''';
comment on column BILL_REC_DAMAGE.RECE_BY
  is '核销人';
comment on column BILL_REC_DAMAGE.RECE_TIME
  is '核销时间';

CREATE OR REPLACE PROCEDURE BMS_REC_LOSS_AUDITAGREE
(in_damage_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS
t_show_seq number;
t_min_show_seq number;
t_id varchar2(32);
op_role_id  varchar(50);
op_customer_id varchar2(32);
op_init_no varchar2(100);
BEGIN

    out_put_result := '00';
    SELECT customer_id into op_customer_id from BILL_REC_DAMAGE where DAMAGE_NO = in_damage_no;
    insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
    (sys_guid(),in_damage_no,'DAMAGE_NO',sys_admin_role,sys_admin_id,sysdate,'已审批',notes,sysdate,sys_admin_id);

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'DAMAGE_NO' and t1.customer_id = op_customer_id;

        select show_seq  into  t_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and role_id = sys_admin_role;
       
    EXCEPTION
       WHEN NO_DATA_FOUND THEN
       t_show_seq :=0;
    END;

    if t_show_seq!=0 then

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'DAMAGE_NO' and t1.customer_id = op_customer_id;

        select min(show_seq) into t_min_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ >t_show_seq;
        EXCEPTION
    WHEN NO_DATA_FOUND THEN
        t_min_show_seq := 0;
    END;

    if t_min_show_seq!=0 then
       begin
          select ROLE_ID into op_role_id from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ=t_min_show_seq;
       EXCEPTION
          WHEN NO_DATA_FOUND THEN
          op_role_id :='错误';
       END;
       if op_role_id!='错误' then
            update  BILL_REC_DAMAGE set ROLE_ID=op_role_id  where DAMAGE_NO=in_damage_no;
       else
            out_put_result :='01角色数据异常';
       end if;

     else
        update BILL_REC_DAMAGE set status='30',ROLE_ID='' where DAMAGE_NO=in_damage_no;
     end if;

   else
      out_put_result :='01数据异常';
   end if;

commit;

Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_REC_LOSS_AUDITBACK (
 in_damage_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS

BEGIN
        out_put_result :='00';
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
        (sys_guid(),in_damage_no,'DAMAGE_NO',sys_admin_role,sys_admin_id,sysdate,'已打回',notes,sysdate,sys_admin_id);

         update  BILL_REC_DAMAGE set status='15',ROLE_ID='' where DAMAGE_NO=in_damage_no;

commit;
Exception
        WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_REC_ADJNO_AUDITAGREE
(in_doc_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS
t_show_seq number;
t_min_show_seq number;
t_id varchar2(32);
op_role_id  varchar(50);

op_init_no varchar2(100);
op_customer_id varchar2(32);
BEGIN

    out_put_result := '00';
    
    SELECT buss_id into op_customer_id from BILL_REC_ADJUST where ADJ_NO = in_doc_no;
    
    insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
    (sys_guid(),in_doc_no,'REC_ADJNO',sys_admin_role,sys_admin_id,sysdate,'已审批',notes,sysdate,sys_admin_id);

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_ADJNO' and t1.customer_id = op_customer_id;

        select show_seq  into  t_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and role_id = sys_admin_role;

    EXCEPTION
       WHEN NO_DATA_FOUND THEN
       t_show_seq :=0;
    END;

    if t_show_seq!=0 then

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_ADJNO' and t1.customer_id = op_customer_id;

        select min(show_seq) into t_min_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ >t_show_seq;
        EXCEPTION
    WHEN NO_DATA_FOUND THEN
        t_min_show_seq := 0;
    END;

    if t_min_show_seq!=0 then
       begin
          select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_ADJNO' and t1.customer_id = op_customer_id;

          select ROLE_ID into op_role_id from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ=t_min_show_seq;
       EXCEPTION
          WHEN NO_DATA_FOUND THEN
          op_role_id :='错误';
       END;
       if op_role_id!='错误' then
            update  BILL_REC_ADJUST set ROLE_ID=op_role_id  where ADJ_NO=in_doc_no;
       else
            out_put_result :='角色数据异常';
       end if;

     else
        update BILL_REC_ADJUST set status='30',ROLE_ID='' where ADJ_NO=in_doc_no;
        select INIT_NO into op_init_no FROM BILL_REC_ADJUST where ADJ_NO = IN_DOC_NO;
        update BILL_REC_INITIAL SET HOLD_FLAG = 'N',INITITAL_AMOUNT = CONFIRM_AMOUNT,ADJ_AMOUNT = 0 where INIT_NO = op_init_no;
        update BILL_REC_INITDETAILS SET TOT_AMOUNT = CONFIRM_AMOUNT,ADJ_AMOUNT = 0 WHERE INIT_NO = op_init_no;
     end if;

   else
      out_put_result :='数据异常';
   end if;

commit;

Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_REC_ADJNO_AUDITBACK
(
 in_doc_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS

BEGIN
        out_put_result :='00';
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
        (sys_guid(),in_doc_no,'REC_ADJNO',sys_admin_role,sys_admin_id,sysdate,'已打回',notes,sysdate,sys_admin_id);

       update  BILL_REC_ADJUST set status='15',ROLE_ID='' where ADJ_NO=in_doc_no;


commit;
Exception
        WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_REC_INVOICE_AUDITAGREE
(in_req_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS
t_show_seq number;
t_min_show_seq number;
t_id varchar2(32);
op_role_id  varchar(50);
op_customer_id varchar2(32);
BEGIN

    out_put_result := '00';
    
    SELECT buss_id into op_customer_id from BILL_REC_INVOICE where INVOICE_NO = in_req_no;
    
    insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
    (sys_guid(),in_req_no,'REC_INVOICENO',sys_admin_role,sys_admin_id,sysdate,'已审批',notes,sysdate,sys_admin_id);

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_INVOICENO' and t1.customer_id = op_customer_id;

        select show_seq  into  t_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and role_id = sys_admin_role;

    EXCEPTION
       WHEN NO_DATA_FOUND THEN
       t_show_seq :=0;
    END;

    if t_show_seq!=0 then

    begin
        select min(show_seq) into t_min_show_seq from SYS_APPROVE_SET where SHOW_SEQ >t_show_seq;
        EXCEPTION
    WHEN NO_DATA_FOUND THEN
        t_min_show_seq := 0;
    END;

    if t_min_show_seq!=0 then
       begin
          select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_INVOICENO' and t1.customer_id = op_customer_id;

          select ROLE_ID into op_role_id from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ=t_min_show_seq;
       EXCEPTION
          WHEN NO_DATA_FOUND THEN
          op_role_id :='错误';
       END;
       if op_role_id!='错误' then
            update  BILL_REC_INVOICE set ROLE_ID=op_role_id  where INVOICE_NO=in_req_no;
       else
            out_put_result :='角色数据异常';
       end if;

     else
        update  BILL_REC_INVOICE set status='30',ROLE_ID='' where INVOICE_NO=in_req_no;
     end if;

   else
      out_put_result :='数据异常';
   end if;

commit;

Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_REC_INVOICE_AUDITBACK
(
 in_req_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS

BEGIN
        out_put_result :='00';
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
        (sys_guid(),in_req_no,'REC_INVOICENO',sys_admin_role,sys_admin_id,sysdate,'已打回',notes,sysdate,sys_admin_id);

       update  BILL_REC_INVOICE set status='15',ROLE_ID='' where INVOICE_NO=in_req_no;


commit;
Exception
        WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_ADJNO_AUDITAGREE
(in_doc_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS
t_show_seq number;
t_min_show_seq number;
t_id varchar2(32);

op_role_id  varchar(50);
op_customer_id varchar2(32);
op_init_no varchar2(100);
BEGIN

    out_put_result := '00';
    select customer_id into op_customer_id from BILL_PAY_ADJUST where ADJ_NO = in_doc_no;
    insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
    (sys_guid(),in_doc_no,'PAY_ADJNO',sys_admin_role,sys_admin_id,sysdate,'已审批',notes,sysdate,sys_admin_id);

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_ADJNO' and t1.customer_id = op_customer_id;

        select show_seq  into  t_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and role_id = sys_admin_role;

    EXCEPTION
       WHEN NO_DATA_FOUND THEN
       t_show_seq :=0;
    END;

    if t_show_seq!=0 then

        begin
            select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_ADJNO' and t1.customer_id = op_customer_id;

            select min(show_seq) into t_min_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ >t_show_seq;
            EXCEPTION
        WHEN NO_DATA_FOUND THEN
            t_min_show_seq := 0;
        END;

        if t_min_show_seq!=0 then
           begin
              select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'REC_ADJNO' and t1.customer_id = op_customer_id;

              select ROLE_ID into op_role_id from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ=t_min_show_seq;
           EXCEPTION
              WHEN NO_DATA_FOUND THEN
              op_role_id :='错误';
           END;
           if op_role_id!='错误' then
                update  BILL_PAY_ADJUST set ROLE_ID=op_role_id  where ADJ_NO=in_doc_no;
           else
                out_put_result :='角色数据异常';
           end if;

         else
            update  BILL_PAY_ADJUST set status='30',ROLE_ID='' where ADJ_NO=in_doc_no;
            select INIT_NO into op_init_no FROM BILL_PAY_ADJUST where ADJ_NO = IN_DOC_NO;
            update BILL_PAY_INITIAL SET HOLD_FLAG = 'N',INITITAL_AMOUNT = CONFIRM_AMOUNT,ADJ_AMOUNT = 0 where INIT_NO = op_init_no;
            update BILL_PAY_INITDETAILS SET TOT_AMOUNT = CONFIRM_AMOUNT,ADJ_AMOUNT = 0 WHERE INIT_NO = op_init_no;
         end if;

     else
        out_put_result :='数据异常,当前角色无审批权限';
     end if;

     commit;

Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;

END;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_ADJNO_AUDITBACK (
 in_doc_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS

BEGIN
        out_put_result :='00';
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
        (sys_guid(),in_doc_no,'PAY_ADJNO',sys_admin_role,sys_admin_id,sysdate,'已打回',notes,sysdate,sys_admin_id);

         update  BILL_PAY_ADJUST set status='15',ROLE_ID='' where ADJ_NO=in_doc_no;

commit;
Exception
        WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_REQNO_AUDITAGREE
(in_req_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS
t_show_seq number;
t_min_show_seq number;
t_id varchar2(32);

op_customer_id varchar2(32);
op_role_id  varchar(50);
BEGIN

    out_put_result := '00';
    
    SELECT customer_id into op_customer_id from BILL_PAY_REQUEST where REQ_NO = in_req_no;

    insert into SYS_APPROVE_LOG (ID,DOC_NO,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
    (sys_guid(),in_req_no,sys_admin_role,sys_admin_id,sysdate,'已审批',notes,sysdate,sys_admin_id);

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'PAY_REQNO' and t1.customer_id = op_customer_id;

        select show_seq  into  t_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and role_id = sys_admin_role;

    EXCEPTION
       WHEN NO_DATA_FOUND THEN
       t_show_seq :=0;
    END;

    if t_show_seq!=0 then

    begin
        select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'PAY_REQNO' and t1.customer_id = op_customer_id;

        select min(show_seq) into t_min_show_seq from SYS_APPROVE_SET where HEAD_ID = t_id and SHOW_SEQ >t_show_seq;
        EXCEPTION
    WHEN NO_DATA_FOUND THEN
        t_min_show_seq := 0;
    END;

    if t_min_show_seq!=0 then
       begin       
          select t.id into t_id from sys_approve_head t,sys_approve_customer t1 where t.id = t1.head_id and t.doc_no = 'PAY_REQNO' and t1.customer_id = op_customer_id;

          select ROLE_ID into op_role_id from SYS_APPROVE_SET where  HEAD_ID = t_id and SHOW_SEQ=t_min_show_seq;
       EXCEPTION
          WHEN NO_DATA_FOUND THEN
          op_role_id :='错误';
       END;
       if op_role_id!='错误' then
            update  BILL_PAY_REQUEST set ROLE_ID=op_role_id  where REQ_NO=in_req_no;
       else
            out_put_result :='角色数据异常';
       end if;

     else
        update  BILL_PAY_REQUEST set status='30',ROLE_ID='' where REQ_NO=in_req_no;
     end if;

   else
      out_put_result :='数据异常';
   end if;

commit;

Exception
     WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

CREATE OR REPLACE PROCEDURE BMS_PAY_REQNO_AUDITBACK
(
 in_req_no IN VARCHAR2
, sys_admin_role IN VARCHAR2
, sys_admin_id IN VARCHAR2
, notes IN VARCHAR2
,out_put_result out varchar2
)
IS

BEGIN
        out_put_result :='00';
        insert into SYS_APPROVE_LOG (ID,DOC_NO,DOC_TYPE,ROLE_ID,APPROVER,APPROVE_TIME,APPROVER_RESULT,NOTES,ADDTIME,ADDWHO)values
        (sys_guid(),in_req_no,'PAY_REQNO',sys_admin_role,sys_admin_id,sysdate,'已打回',notes,sysdate,sys_admin_id);

       update  BILL_PAY_REQUEST set status='15',ROLE_ID='' where REQ_NO=in_req_no;
commit;
Exception
        WHEN OTHERS THEN
                out_put_result :='01'||sqlcode || sqlerrm; --失败标记
                ROLLBACK;
END;

/

--caijiante 2017-05-17 sys_func_page
985?	22986	P09_F042_P1	P09_F042	申请单明细	150	Y	2015/5/27	wpsadmin	申请单明细	B	
986?	22987	P09_F042_P2	P09_F042	发票信息	150	Y	2015/5/27	wpsadmin	发票信息	B	
987?	22988	P09_F042_P3	P09_F042	核销信息	150	Y	2015/5/27	wpsadmin	核销信息	B	
988?	22989	P09_F042_P4	P09_F042	审批日志	150	Y	2015/5/27	wpsadmin	审批日志	B	
989?	22990	P09_F058_P1	P09_F058	申请单明细	150	Y	2015/5/27	wpsadmin	申请单明细	B	
990?	22991	P09_F058_P2	P09_F058	发票信息	150	Y	2015/5/27	wpsadmin	发票信息	B	
991?	22992	P09_F058_P3	P09_F058	核销信息	150	Y	2015/5/27	wpsadmin	核销信息	B	
992?	22993	P09_F058_P4	P09_F058	发票图片	150	Y	2015/5/27	wpsadmin	发票图片	B	
993?	22994	P09_F058_P5	P09_F058	审批日志	150	Y	2015/5/27	wpsadmin	审批日志	B	


--lml 2017-05-17 sys_func_page
22995	P09_F045_P0_03	P09_F045_P0	发票新增	150	Y	27-5月 -15	wpsadmin	发票新增	B
22996	P09_F045_P0_04	P09_F045_P0	发票保存	150	Y	27-5月 -15	wpsadmin	发票保存	B
22997	P09_F045_P0_05	P09_F045_P0	发票删除	150	Y	27-5月 -15	wpsadmin	发票删除	B
22998	P09_F045_P0_06	P09_F045_P0	发票取消	150	Y	27-5月 -15	wpsadmin	发票取消	B
22999	P09_F045_P0_07	P09_F045_P0	核销	150	Y	27-5月 -15	wpsadmin	核销	B

--yuanlei 2017-05-18
-- Create table
create table BILL_REC_DMGINVOICEINFO
(
  ID              VARCHAR2(32),
  INVOICE_NUM     VARCHAR2(50),
  INVOICE_TYPE    VARCHAR2(32),
  INVOICE_ADDRESS VARCHAR2(300),
  BILL_TO         VARCHAR2(100),
  TAX_NO          VARCHAR2(50),
  BANK_POINT      VARCHAR2(150),
  BANK_ACCOUNT    VARCHAR2(50),
  FEE_NAME        VARCHAR2(50),
  AMOUNT          NUMBER(18,8),
  TAX_RATIO       NUMBER(18,8),
  TAX_AMOUNT      NUMBER(18,8),
  ACT_AMOUNT      NUMBER(18,8),
  INVOICE_BY      VARCHAR2(50),
  INVOICE_TIME    DATE,
  NOTES           VARCHAR2(300),
  ADDTIME         DATE not null,
  ADDWHO          VARCHAR2(20) not null,
  EDITTIME        DATE,
  EDITWHO         VARCHAR2(20),
  DAMAGE_NO       VARCHAR2(100),
  RECE_AMOUNT     NUMBER(18,8) default 0,
  RECE_BY         VARCHAR2(20),
  RECE_TIME       DATE,
  EXPRESS_NO      VARCHAR2(50)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table BILL_REC_DMGINVOICEINFO
  is '应付发票信息';
-- Add comments to the columns 
comment on column BILL_REC_DMGINVOICEINFO.INVOICE_NUM
  is '发票号';
comment on column BILL_REC_DMGINVOICEINFO.INVOICE_TYPE
  is '发票类型CODEID = ''INVOICE_TYP''';
comment on column BILL_REC_DMGINVOICEINFO.INVOICE_ADDRESS
  is '地址电话';
comment on column BILL_REC_DMGINVOICEINFO.BILL_TO
  is '发票抬头';
comment on column BILL_REC_DMGINVOICEINFO.TAX_NO
  is '税号';
comment on column BILL_REC_DMGINVOICEINFO.BANK_POINT
  is '开户银行';
comment on column BILL_REC_DMGINVOICEINFO.BANK_ACCOUNT
  is '银行帐号';
comment on column BILL_REC_DMGINVOICEINFO.FEE_NAME
  is '费用名称';
comment on column BILL_REC_DMGINVOICEINFO.AMOUNT
  is '金额';
comment on column BILL_REC_DMGINVOICEINFO.TAX_RATIO
  is '税率';
comment on column BILL_REC_DMGINVOICEINFO.TAX_AMOUNT
  is '税额';
comment on column BILL_REC_DMGINVOICEINFO.ACT_AMOUNT
  is '总金额';
comment on column BILL_REC_DMGINVOICEINFO.INVOICE_BY
  is '开票人';
comment on column BILL_REC_DMGINVOICEINFO.INVOICE_TIME
  is '开票时间';
comment on column BILL_REC_DMGINVOICEINFO.NOTES
  is '备注';
comment on column BILL_REC_DMGINVOICEINFO.DAMAGE_NO
  is '赔偿单号';
comment on column BILL_REC_DMGINVOICEINFO.RECE_AMOUNT
  is '核销金额';
comment on column BILL_REC_DMGINVOICEINFO.RECE_BY
  is '核销人';
comment on column BILL_REC_DMGINVOICEINFO.RECE_TIME
  is '核销时间';
comment on column BILL_REC_DMGINVOICEINFO.EXPRESS_NO
  is '快递地址';

-- Create table
create table BILL_REC_DMGRECELOG
(
  ID              VARCHAR2(32) not null,
  DAMAGE_NO       VARCHAR2(50),
  INVOICE_NUM     VARCHAR2(50),
  RECE_BY         VARCHAR2(50),
  RECE_TIME       DATE,
  RECE_AMOUNT     NUMBER(18,8),
  ADDTIME         DATE not null,
  ADDWHO          VARCHAR2(20) not null,
  EDITTIME        DATE,
  EDITWHO         VARCHAR2(20),
  COLLECTION_TIME DATE,
  VOUCHER_NO      VARCHAR2(200)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column BILL_REC_DMGRECELOG.DAMAGE_NO
  is '赔偿单号';
comment on column BILL_REC_DMGRECELOG.INVOICE_NUM
  is '发票号';
comment on column BILL_REC_DMGRECELOG.RECE_BY
  is '核销人';
comment on column BILL_REC_DMGRECELOG.RECE_TIME
  is '核销时间';
comment on column BILL_REC_DMGRECELOG.RECE_AMOUNT
  is '核销金额';
comment on column BILL_REC_DMGRECELOG.COLLECTION_TIME
  is '收款日期';
comment on column BILL_REC_DMGRECELOG.VOUCHER_NO
  is '凭证号';

alter table trans_load_header modify(udf3 varchar2(1000));
alter table bms_load_header modify(udf3 varchar2(1000));
alter table trans_shipment_header add UNLOAD_FLAG CHAR(1) default 'N';
alter table BMS_shipment_header add UNLOAD_FLAG CHAR(1) NULL;
alter table TRANS_ORDER_HEADER modify(BIZ_CODE varchar2(1000));
alter table BMS_ORDER_HEADER modify(BIZ_CODE varchar2(1000));
alter table TRANS_SHIPMENT_HEADER modify(BIZ_CODE varchar2(1000));
alter table BMS_SHIPMENT_HEADER modify(BIZ_CODE varchar2(1000));

create or replace trigger SYNC_SHIPMENT_HEADER after insert or update or delete
on TRANS_SHIPMENT_HEADER for each row
declare
    integrity_error exception;
    errno            integer;
    errmsg           char(200);

begin
if inserting then
    insert into BMS_SHIPMENT_HEADER(ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,ODR_TYP,ODR_TIME,
    LOAD_NO,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,FROM_LOAD_TIME,
    PRE_LOAD_TIME,DEPART_TIME,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,
    UNLOAD_TEL,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,POD_FLAG,PRE_POD_TIME,STATUS,BILL_TO,REFENENCE1,REFENENCE2,
    REFENENCE3,REFENENCE4,CREATE_ORG_ID,EXEC_ORG_ID,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,COD_FLAG,TOT_QNTY,
    TRANS_UOM,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,
    SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER,MOBILE,ABNOMAL_STAT,SPLIT_FLAG,
    SPLIT_REASON_CODE,SPLIT_REASON,ADDTIME,ADDWHO,EDITTIME,EDITWHO,ASSIGN_STAT,ABNOMAL_NOTES,LOSDAM_FLAG,
    START_LOAD_TIME,END_LOAD_TIME,UNLOAD_TIME,POD_TIME,FREEZE_NOTES,OP_LOAD_TIME,SIGNATARY,OP_UNLOAD_TIME,
    OP_POD_TIME,LOAD_SEQ,UNLOAD_SEQ,LOAD_DELAY_REASON,UNLOAD_DELAY_REASON,POD_DELAY_REASON,CURRENT_LOC,WHSE_ID,
    UNLOAD_DELAY_DAYS,POD_DELAY_DAYS,SATISFY_CODE,SERVICE_CODE,LOAD_PRINT_COUNT,SHPM_PRINT_COUNT,PRE_WHSE_TIME,
    PRE_ASSIGN_TIME,ASSIGN_TIME,PRE_END_LOAD_TIME,FACT_MILE,SETT_MILE,ROUTE_MILE,PICKING_STAT,TRACK_NOTES,
    LEAVE_WHSE_TIME,ARRIVE_WHSE_TIME,BELONG_JRNY_NO,FIRST_JRNY_NO,LAST_JRNY_NO,SENDABLE_FLAG,END_UNLOAD_ID,
    END_UNLOAD_NAME,END_UNLOAD_ADDRESS,RECE_FLAG,UNION_SHPM_NO,SIGN_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME,
    STATUS_NAME,TRANS_SRVC_ID_NAME,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,
    UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ZIP,SOURCE_NO,END_TEL,END_CONTACT,
    SIGN_ORG_ID_NAME,BIZ_TYP,UDF1,UDF2,UDF3,UDF4,LOAD_NOTES,LOAD_STAT,UDF5,UDF6,UDF7,UDF8,LOAD_REGION,
    UNLOAD_REGION,VEH_POS,BUK_FLAG,LOAD_CODE,UNLOAD_CODE,BIZ_CODE,RDC_NO,PAY_TYP,SETTLE_TYP,PRE_ORDER_NO,
    TRANS_TYPE,OP_PICKUP_TIME,TEMPERATURE1,TEMPERATURE2,UPLOAD_FLAG,START_UNLOAD_TIME,CAST_BILL_TIME,UNLOAD_FLAG)
    
    values(:NEW.ID,:NEW.SHPM_NO,:NEW.ODR_NO,:NEW.PARN_SHPM_NO,:NEW.CUSTOM_ODR_NO,:NEW.CUSTOMER_ID,:NEW.CUSTOMER_NAME,
    :NEW.TRANS_SRVC_ID,:NEW.ODR_TYP,:NEW.ODR_TIME,:NEW.LOAD_NO,:NEW.LOAD_ID,:NEW.LOAD_NAME,:NEW.LOAD_AREA_ID,
    :NEW.LOAD_AREA_NAME,:NEW.LOAD_ADDRESS,:NEW.LOAD_CONTACT,:NEW.LOAD_TEL,:NEW.FROM_LOAD_TIME,:NEW.PRE_LOAD_TIME,
    :NEW.DEPART_TIME,:NEW.UNLOAD_ID,:NEW.UNLOAD_NAME,:NEW.UNLOAD_AREA_ID,:NEW.UNLOAD_AREA_NAME,:NEW.UNLOAD_ADDRESS,
    :NEW.UNLOAD_CONTACT,:NEW.UNLOAD_TEL,:NEW.FROM_UNLOAD_TIME,:NEW.PRE_UNLOAD_TIME,:NEW.POD_FLAG,:NEW.PRE_POD_TIME,
    :NEW.STATUS,:NEW.BILL_TO,:NEW.REFENENCE1,:NEW.REFENENCE2,:NEW.REFENENCE3,:NEW.REFENENCE4,:NEW.CREATE_ORG_ID,
    :NEW.EXEC_ORG_ID,:NEW.UGRT_GRD,:NEW.SALES_MAN,:NEW.ENALBLE_FLAG,:NEW.BTCH_NUM,:NEW.COD_FLAG,:NEW.TOT_QNTY,
    :NEW.TRANS_UOM,:NEW.TOT_GROSS_W,:NEW.TOT_VOL,:NEW.TOT_NET_W,:NEW.TOT_WORTH,:NEW.TOT_QNTY_EACH,:NEW.ROUTE_ID,
    :NEW.PRINT_FLAG,:NEW.SHOW_SEQ,:NEW.SLF_DELIVER_FLAG,:NEW.SLF_PICKUP_FLAG,:NEW.NOTES,:NEW.SUPLR_ID,:NEW.SUPLR_NAME,
    :NEW.VEHICLE_TYP_ID,:NEW.PLATE_NO,:NEW.DRIVER,:NEW.MOBILE,:NEW.ABNOMAL_STAT,:NEW.SPLIT_FLAG,:NEW.SPLIT_REASON_CODE,
    :NEW.SPLIT_REASON,:NEW.ADDTIME,:NEW.ADDWHO,:NEW.EDITTIME,:NEW.EDITWHO,:NEW.ASSIGN_STAT,:NEW.ABNOMAL_NOTES,
    :NEW.LOSDAM_FLAG,:NEW.START_LOAD_TIME,:NEW.END_LOAD_TIME,:NEW.UNLOAD_TIME,:NEW.POD_TIME,:NEW.FREEZE_NOTES,
    :NEW.OP_LOAD_TIME,:NEW.SIGNATARY,:NEW.OP_UNLOAD_TIME,:NEW.OP_POD_TIME,:NEW.LOAD_SEQ,:NEW.UNLOAD_SEQ,
    :NEW.LOAD_DELAY_REASON,:NEW.UNLOAD_DELAY_REASON,:NEW.POD_DELAY_REASON,:NEW.CURRENT_LOC,:NEW.WHSE_ID,
    :NEW.UNLOAD_DELAY_DAYS,:NEW.POD_DELAY_DAYS,:NEW.SATISFY_CODE,:NEW.SERVICE_CODE,:NEW.LOAD_PRINT_COUNT,
    :NEW.SHPM_PRINT_COUNT,:NEW.PRE_WHSE_TIME,:NEW.PRE_ASSIGN_TIME,:NEW.ASSIGN_TIME,:NEW.PRE_END_LOAD_TIME,:NEW.FACT_MILE,
    :NEW.SETT_MILE,:NEW.ROUTE_MILE,:NEW.PICKING_STAT,:NEW.TRACK_NOTES,:NEW.LEAVE_WHSE_TIME,:NEW.ARRIVE_WHSE_TIME,
    :NEW.BELONG_JRNY_NO,:NEW.FIRST_JRNY_NO,:NEW.LAST_JRNY_NO,:NEW.SENDABLE_FLAG,:NEW.END_UNLOAD_ID,:NEW.END_UNLOAD_NAME,
    :NEW.END_UNLOAD_ADDRESS,:NEW.RECE_FLAG,:NEW.UNION_SHPM_NO,:NEW.SIGN_ORG_ID,:NEW.CREATE_ORG_ID_NAME,:NEW.EXEC_ORG_ID_NAME,
    :NEW.STATUS_NAME,:NEW.TRANS_SRVC_ID_NAME,:NEW.EXEC_STAT,:NEW.LOAD_ZIP,:NEW.LOAD_AREA_ID2,:NEW.LOAD_AREA_NAME2,
    :NEW.LOAD_AREA_ID3,:NEW.LOAD_AREA_NAME3,:NEW.UNLOAD_AREA_ID2,:NEW.UNLOAD_AREA_NAME2,:NEW.UNLOAD_AREA_ID3,
    :NEW.UNLOAD_AREA_NAME3,:NEW.UNLOAD_ZIP,:NEW.SOURCE_NO,:NEW.END_TEL,:NEW.END_CONTACT,:NEW.SIGN_ORG_ID_NAME,
    :NEW.BIZ_TYP,:NEW.UDF1,:NEW.UDF2,:NEW.UDF3,:NEW.UDF4,:NEW.LOAD_NOTES,:NEW.LOAD_STAT,:NEW.UDF5,:NEW.UDF6,:NEW.UDF7,
    :NEW.UDF8,:NEW.LOAD_REGION,:NEW.UNLOAD_REGION,:NEW.VEH_POS,:NEW.BUK_FLAG,:NEW.LOAD_CODE,:NEW.UNLOAD_CODE,:NEW.BIZ_CODE,
    :NEW.RDC_NO,:NEW.PAY_TYP,:NEW.SETTLE_TYP,:NEW.PRE_ORDER_NO,:NEW.TRANS_TYPE,:NEW.OP_PICKUP_TIME,:NEW.TEMPERATURE1,
    :NEW.TEMPERATURE2,:NEW.UPLOAD_FLAG,:NEW.START_UNLOAD_TIME,:NEW.CAST_BILL_TIME,:NEW.UNLOAD_FLAG);
elsif updating then
    update BMS_SHIPMENT_HEADER set SHPM_NO=:NEW.SHPM_NO,ODR_NO=:NEW.ODR_NO,PARN_SHPM_NO=:NEW.PARN_SHPM_NO,
    CUSTOM_ODR_NO=:NEW.CUSTOM_ODR_NO,CUSTOMER_ID=:NEW.CUSTOMER_ID,CUSTOMER_NAME=:NEW.CUSTOMER_NAME,
    TRANS_SRVC_ID=:NEW.TRANS_SRVC_ID,ODR_TYP=:NEW.ODR_TYP,ODR_TIME=:NEW.ODR_TIME,LOAD_NO=:NEW.LOAD_NO,
    LOAD_ID=:NEW.LOAD_ID,LOAD_NAME=:NEW.LOAD_NAME,LOAD_AREA_ID=:NEW.LOAD_AREA_ID,LOAD_AREA_NAME=:NEW.LOAD_AREA_NAME,
    LOAD_ADDRESS=:NEW.LOAD_ADDRESS,LOAD_CONTACT=:NEW.LOAD_CONTACT,LOAD_TEL=:NEW.LOAD_TEL,FROM_LOAD_TIME=:NEW.FROM_LOAD_TIME,
    PRE_LOAD_TIME=:NEW.PRE_LOAD_TIME,DEPART_TIME=:NEW.DEPART_TIME,UNLOAD_ID=:NEW.UNLOAD_ID,UNLOAD_NAME=:NEW.UNLOAD_NAME,
    UNLOAD_AREA_ID=:NEW.UNLOAD_AREA_ID,UNLOAD_AREA_NAME=:NEW.UNLOAD_AREA_NAME,UNLOAD_ADDRESS=:NEW.UNLOAD_ADDRESS,
    UNLOAD_CONTACT=:NEW.UNLOAD_CONTACT,UNLOAD_TEL=:NEW.UNLOAD_TEL,FROM_UNLOAD_TIME=:NEW.FROM_UNLOAD_TIME,
    PRE_UNLOAD_TIME=:NEW.PRE_UNLOAD_TIME,POD_FLAG=:NEW.POD_FLAG,PRE_POD_TIME=:NEW.PRE_POD_TIME,STATUS=:NEW.STATUS,
    BILL_TO=:NEW.BILL_TO,REFENENCE1=:NEW.REFENENCE1,REFENENCE2=:NEW.REFENENCE2,REFENENCE3=:NEW.REFENENCE3,
    REFENENCE4=:NEW.REFENENCE4,CREATE_ORG_ID=:NEW.CREATE_ORG_ID,EXEC_ORG_ID=:NEW.EXEC_ORG_ID,UGRT_GRD=:NEW.UGRT_GRD,
    SALES_MAN=:NEW.SALES_MAN,ENALBLE_FLAG=:NEW.ENALBLE_FLAG,BTCH_NUM=:NEW.BTCH_NUM,COD_FLAG=:NEW.COD_FLAG,TOT_QNTY=:NEW.TOT_QNTY,
    TRANS_UOM=:NEW.TRANS_UOM,TOT_GROSS_W=:NEW.TOT_GROSS_W,TOT_VOL=:NEW.TOT_VOL,TOT_NET_W=:NEW.TOT_NET_W,
    TOT_WORTH=:NEW.TOT_WORTH,TOT_QNTY_EACH=:NEW.TOT_QNTY_EACH,ROUTE_ID=:NEW.ROUTE_ID,PRINT_FLAG=:NEW.PRINT_FLAG,
    SHOW_SEQ=:NEW.SHOW_SEQ,SLF_DELIVER_FLAG=:NEW.SLF_DELIVER_FLAG,SLF_PICKUP_FLAG=:NEW.SLF_PICKUP_FLAG,
    NOTES=:NEW.NOTES,SUPLR_ID=:NEW.SUPLR_ID,SUPLR_NAME=:NEW.SUPLR_NAME,VEHICLE_TYP_ID=:NEW.VEHICLE_TYP_ID,
    PLATE_NO=:NEW.PLATE_NO,DRIVER=:NEW.DRIVER,MOBILE=:NEW.MOBILE,ABNOMAL_STAT=:NEW.ABNOMAL_STAT,SPLIT_FLAG=:NEW.SPLIT_FLAG,
    SPLIT_REASON_CODE=:NEW.SPLIT_REASON_CODE,SPLIT_REASON=:NEW.SPLIT_REASON,ADDTIME=:NEW.ADDTIME,ADDWHO=:NEW.ADDWHO,
    EDITTIME=:NEW.EDITTIME,EDITWHO=:NEW.EDITWHO,ASSIGN_STAT=:NEW.ASSIGN_STAT,ABNOMAL_NOTES=:NEW.ABNOMAL_NOTES,
    LOSDAM_FLAG=:NEW.LOSDAM_FLAG,START_LOAD_TIME=:NEW.START_LOAD_TIME,END_LOAD_TIME=:NEW.END_LOAD_TIME,
    UNLOAD_TIME=:NEW.UNLOAD_TIME,POD_TIME=:NEW.POD_TIME,FREEZE_NOTES=:NEW.FREEZE_NOTES,OP_LOAD_TIME=:NEW.OP_LOAD_TIME,
    SIGNATARY=:NEW.SIGNATARY,OP_UNLOAD_TIME=:NEW.OP_UNLOAD_TIME,OP_POD_TIME=:NEW.OP_POD_TIME,LOAD_SEQ=:NEW.LOAD_SEQ,
    UNLOAD_SEQ=:NEW.UNLOAD_SEQ,LOAD_DELAY_REASON=:NEW.LOAD_DELAY_REASON,UNLOAD_DELAY_REASON=:NEW.UNLOAD_DELAY_REASON,
    POD_DELAY_REASON=:NEW.POD_DELAY_REASON,CURRENT_LOC=:NEW.CURRENT_LOC,WHSE_ID=:NEW.WHSE_ID,UNLOAD_DELAY_DAYS=:NEW.UNLOAD_DELAY_DAYS,
    POD_DELAY_DAYS=:NEW.POD_DELAY_DAYS,SATISFY_CODE=:NEW.SATISFY_CODE,SERVICE_CODE=:NEW.SERVICE_CODE,
    LOAD_PRINT_COUNT=:NEW.LOAD_PRINT_COUNT,SHPM_PRINT_COUNT=:NEW.SHPM_PRINT_COUNT,PRE_WHSE_TIME=:NEW.PRE_WHSE_TIME,
    PRE_ASSIGN_TIME=:NEW.PRE_ASSIGN_TIME,ASSIGN_TIME=:NEW.ASSIGN_TIME,PRE_END_LOAD_TIME=:NEW.PRE_END_LOAD_TIME,
    FACT_MILE=:NEW.FACT_MILE,SETT_MILE=:NEW.SETT_MILE,ROUTE_MILE=:NEW.ROUTE_MILE,PICKING_STAT=:NEW.PICKING_STAT,
    TRACK_NOTES=:NEW.TRACK_NOTES,LEAVE_WHSE_TIME=:NEW.LEAVE_WHSE_TIME,ARRIVE_WHSE_TIME=:NEW.ARRIVE_WHSE_TIME,
    BELONG_JRNY_NO=:NEW.BELONG_JRNY_NO,FIRST_JRNY_NO=:NEW.FIRST_JRNY_NO,LAST_JRNY_NO=:NEW.LAST_JRNY_NO,
    SENDABLE_FLAG=:NEW.SENDABLE_FLAG,END_UNLOAD_ID=:NEW.END_UNLOAD_ID,END_UNLOAD_NAME=:NEW.END_UNLOAD_NAME,
    END_UNLOAD_ADDRESS=:NEW.END_UNLOAD_ADDRESS,RECE_FLAG=:NEW.RECE_FLAG,UNION_SHPM_NO=:NEW.UNION_SHPM_NO,
    SIGN_ORG_ID=:NEW.SIGN_ORG_ID,CREATE_ORG_ID_NAME=:NEW.CREATE_ORG_ID_NAME,EXEC_ORG_ID_NAME=:NEW.EXEC_ORG_ID_NAME,
    STATUS_NAME=:NEW.STATUS_NAME,TRANS_SRVC_ID_NAME=:NEW.TRANS_SRVC_ID_NAME,EXEC_STAT=:NEW.EXEC_STAT,
    LOAD_ZIP=:NEW.LOAD_ZIP,LOAD_AREA_ID2=:NEW.LOAD_AREA_ID2,LOAD_AREA_NAME2=:NEW.LOAD_AREA_NAME2,
    LOAD_AREA_ID3=:NEW.LOAD_AREA_ID3,LOAD_AREA_NAME3=:NEW.LOAD_AREA_NAME3,UNLOAD_AREA_ID2=:NEW.UNLOAD_AREA_ID2,
    UNLOAD_AREA_NAME2=:NEW.UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3=:NEW.UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3=:NEW.UNLOAD_AREA_NAME3,
    UNLOAD_ZIP=:NEW.UNLOAD_ZIP,SOURCE_NO=:NEW.SOURCE_NO,END_TEL=:NEW.END_TEL,END_CONTACT=:NEW.END_CONTACT,
    SIGN_ORG_ID_NAME=:NEW.SIGN_ORG_ID_NAME,BIZ_TYP=:NEW.BIZ_TYP,UDF1=:NEW.UDF1,UDF2=:NEW.UDF2,UDF3=:NEW.UDF3,
    UDF4=:NEW.UDF4,LOAD_NOTES=:NEW.LOAD_NOTES,LOAD_STAT=:NEW.LOAD_STAT,UDF5=:NEW.UDF5,UDF6=:NEW.UDF6,UDF7=:NEW.UDF7,
    UDF8=:NEW.UDF8,LOAD_REGION=:NEW.LOAD_REGION,UNLOAD_REGION=:NEW.UNLOAD_REGION,VEH_POS=:NEW.VEH_POS,
    BUK_FLAG=:NEW.BUK_FLAG,LOAD_CODE=:NEW.LOAD_CODE,UNLOAD_CODE=:NEW.UNLOAD_CODE,BIZ_CODE=:NEW.BIZ_CODE,
    RDC_NO=:NEW.RDC_NO,PAY_TYP=:NEW.PAY_TYP,SETTLE_TYP=:NEW.SETTLE_TYP,PRE_ORDER_NO=:NEW.PRE_ORDER_NO,
    TRANS_TYPE=:NEW.TRANS_TYPE,OP_PICKUP_TIME=:NEW.OP_PICKUP_TIME,TEMPERATURE1=:NEW.TEMPERATURE1,TEMPERATURE2=:NEW.TEMPERATURE2,
    UPLOAD_FLAG=:NEW.UPLOAD_FLAG,START_UNLOAD_TIME=:NEW.START_UNLOAD_TIME,CAST_BILL_TIME=:NEW.CAST_BILL_TIME,UNLOAD_FLAG=:NEW.UNLOAD_FLAG
    where id=:OLD.id;
elsif deleting then
    delete from BMS_SHIPMENT_HEADER where id=:OLD.id;
end if;
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/

create or replace procedure SP_LOADNO_DISPATCHAUDIT
(
   in_load_no LST,          --调度单号
   in_user_id varchar2,     --登录用户
   output_result out varchar2
   )
 IS
 --in_load_no LST := LST();

 t_load_no varchar2(50);

 t_plate_no varchar(50);
 t_driver varchar2(50);
 t_mobile varchar2(50);
 t_suplr_id varchar(32);
 t_suplr_name varchar2(50);
 t_veh_sign varchar2(50);
 t_exec_org_id varchar2(32);

 op_license varchar2(50);
 op_load_count number(4);
 op_unload_count number(4);
 op_udf2 varchar2(50);
 op_udf3 varchar2(2000);
 op_udf4 varchar2(1000);
 sys_count number;
 t_min_time date;

 t_temp_no1 varchar2(100);
 t_temp_no2 varchar2(100);
 t_gps_no1 varchar2(100);
 
 op_odr_count number(4);

 load_lst LST := LST();
 unload_lst LST := LST();

CURSOR shpm IS
      SELECT ODR_NO,LOAD_NAME,UNLOAD_NAME FROM TRANS_SHIPMENT_HEADER WHERE load_no = t_load_no order by pre_unload_time asc,addtime asc;

 BEGIN
      output_result:='00';--执行成功
      op_udf3 := '';
      op_udf4 := '';
      --in_load_no.extend;
      --in_load_no(1) := '170518044526';
      --dbms_output.put_line('start:' || to_char(sysdate,'yyyy-mm-dd hh:mi:ss'));
      for i in 1..in_load_no.count loop

          t_load_no := in_load_no(i);
          select plate_no,driver1,mobile1,suplr_id,suplr_name,exec_org_id,veh_sign,TEMP_NO1,TEMP_NO2,GPS_NO1
          into t_plate_no,t_driver,t_mobile,t_suplr_id,t_suplr_name,t_exec_org_id,t_veh_sign,t_temp_no1,t_temp_no2,t_gps_no1
          from trans_load_header where load_no = t_load_no;

          for h in shpm loop
              if op_udf3 is null or instr(op_udf3,h.load_name,1) < 1 then
                  load_lst.extend();
                  load_lst(load_lst.count) := h.load_name;
                  if load_lst.count = 1 then
                      op_udf3 := h.load_name;
                  else
                      op_udf3 := op_udf3 || '&' || h.load_name;
                  end if;
              end if;
              if op_udf4 is null or instr(op_udf4,h.unload_name,1) < 1 then
                  unload_lst.extend();
                  unload_lst(unload_lst.count) := h.unload_name;
                  if unload_lst.count = 1 then
                      op_udf4 := h.unload_name;
                  else
                      op_udf4 := op_udf4 || '&' || h.unload_name;
                  end if;
              end if;
          end loop;
          op_load_count := load_lst.count;
          op_unload_count := unload_lst.count;
          op_udf3 := op_udf3 || '-' || op_udf4;
          if length(op_udf3) > 1000 then
              op_udf3 := substr(op_udf3,0,1000);
          end if;
          
          select count(distinct odr_no) into op_odr_count from trans_shipment_header where load_no = t_load_no;
          if op_odr_count > 1 then
              op_udf2 := '整车';
          else
              op_udf2 := '拼车';
          end if;
          --select distinct load_name,unload_name into op_load_name,op_unload_name from trans_shipment_header where load_no = t_load_no;
          --select count(distinct load_id) into op_load_count from trans_shipment_header where load_no = t_load_no;
          --select count(distinct unload_id) into op_unload_count from trans_shipment_header where load_no = t_load_no;
          /*if t_temp_no1 is null and t_temp_no2 is null and t_gps_no1 is null then
              output_result := '01调度单[' || in_load_no(i) || ']必须填写温控设备或GPS!';
              return;
          end if;*/
          if t_plate_no is null then
              output_result := '01调度单[' || in_load_no(i) || ']车牌号为空,不能执行配车确认!';
              return;
          end if;
          if t_suplr_id is null then
              output_result := '01调度单[' || in_load_no(i) || ']供应商为空,不能执行配车确认!';
              return;
          end if;
          --如果调度单下的作业单车牌号为空，把调度单的车牌号带入其作业单中
          --select count(1) into sys_count from trans_shipment_header where load_no = in_load_no(i) and plate_no is null;
          --if sys_count>0 then
             update trans_shipment_header set plate_no = t_plate_no,driver=t_driver,mobile=t_mobile where load_no = t_load_no;
             --update trans_load_job set plate_no = t_plate_no where load_no = in_load_no(i) and load_status = '10' ;
          --end if;
          --UPDATE TRANS_LOAD_HEADER SET dispatch_stat='9EC53E33BDEB4806AAC8CD2904BFD1BC' WHERE load_no = t_load_no;
          --2014-3-4 配车审核后重算调度单数量、体积
          UPDATE TRANS_LOAD_HEADER SET (TOT_GROSS_W, TOT_NET_W, TOT_VOL, TOT_WORTH, TOT_QNTY, TOT_QNTY_EACH,dispatch_stat,dispatch_stat_name,LOAD_COUNT,UNLOAD_COUNT,UDF2,UDF3)
          = ( select SUM(TOT_GROSS_W) as SUM_GROSS_W,SUM(TOT_NET_W) AS SUM_NET_W
            ,SUM(TOT_VOL) AS SUM_VOL,SUM(TOT_WORTH) AS SUM_WORTH,SUM(TOT_QNTY) AS SUM_QNTY,SUM(TOT_QNTY_EACH) AS SUM_QNTY_EACH,'9EC53E33BDEB4806AAC8CD2904BFD1BC','已审核',
            op_load_count,op_unload_count,op_udf2,op_udf3 from TRANS_SHIPMENT_HEADER where TRANS_SHIPMENT_HEADER.LOAD_NO = TRANS_LOAD_HEADER.LOAD_NO
            group by LOAD_NO
            ),PRE_DEPART_TIME = DEPART_TIME where LOAD_NO = t_load_no;

          --插入业务日志
          insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
             values(sys_guid(),RDPG_STAT.DISPATCH_AUDIT,RDPG_STAT.LOAD_NO,t_load_no,'调度单配车审核',sysdate,in_user_id);
          --插入作业单客户日志
          --SP_CUSTOMACT_LOG(RDPG_STAT.DISPATCH_AUDIT,RDPG_STAT.LOAD_NO,t_load_no,in_user_id,sysdate , output_result);
          SP_SFSTATUS_LOG('','',t_load_no,'','030',in_user_id);

          --生成手机APP使用的工号
          select count(1) into sys_count from bas_staff t where staff_name = t_driver and staff_typ = '4D279652C2B9423D8AD958E63B9B3547';-- and org_id = t_exec_org_id;
          if sys_count = 1 then
              select t.oper_license into op_license from bas_staff t where staff_name = t_driver and staff_typ = '4D279652C2B9423D8AD958E63B9B3547';-- and org_id = t_exec_org_id;
              if op_license is null then
                  op_license := 'wpsadmin';
              end if;
              update trans_load_header set drvr_lic_num = op_license where load_no = t_load_no;
          else
              update trans_load_header set drvr_lic_num = 'wpsadmin' where load_no = t_load_no;
          end if;

          begin
              sp_time_calculate('','LOAD_NO','SHPM_NO',t_load_no,1,2,output_result);
              output_result := '00';
              for it in shpm loop
                  SELECT min(leave_whse_time) into t_min_time from TRANS_SHIPMENT_HEADER WHERE odr_no = it.odr_no;
                  --更新原始托运单状态
                  UPDATE TRANS_ORDER_HEADER SET PRE_WHSE_TIME = t_min_time WHERE odr_no = it.odr_no;
              end loop;
          exception when OTHERS THEN
              t_min_time := null;
          end;
         commit;
      end loop;
      --dbms_output.put_line('end:' || to_char(sysdate,'yyyy-mm-dd hh:mi:ss'));
      EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result:='01'||sqlerrm;
      return;
  END;
/

CREATE OR REPLACE PROCEDURE SP_ORDER_CONFIRM (
in_odr_no VARCHAR2,           --订单编号
in_user_id VARCHAR2,          --用户ID
output_result OUT VARCHAR2
)
IS
/**
 * 托运单确认存储过程（批量操作时循环调用）
 * 创建：袁磊 2010-11-23
 */

 --作业单信息
 op_status varchar2(32);               --托运单状态
 op_biz_type varchar2(32);             --业务类型（B2B/B2C/零担)

 op_load_area_id varchar2(32);        --发货（市）
 op_load_area_name varchar2(100);
 op_load_area_id2 varchar2(32);        --发货（市）
 op_load_area_name2 varchar2(100);
 op_load_area_id3 varchar2(32);        --发货（市）
 op_load_area_name3 varchar2(100);
 op_load_id varchar2(32);              --发货仓库
 op_load_code varchar2(32);            --发货代码
 op_load_name varchar2(200);           --发货点
 op_load_contact varchar2(50);
 op_load_tel varchar2(50);
 op_load_address VARCHAR2(300);
 op_udf3 varchar2(2000);
 op_udf4 varchar2(1000);

 op_unload_area_id2 varchar2(32);      --收货（市）

 op_exec_org_id varchar2(32);          --作业单执行机构
 op_exec_org_name varchar2(50);        --执行机构名称
 op_suplr_id varchar2(32);             --默认供应商
 op_suplr_name varchar2(50);           --供应商名称
 op_trans_srvc_id varchar2(32);        --运输服务
 op_trans_srvc_name varchar2(50);
 op_slf_pickup_flag char(1);           --客户自提
 op_slf_deliver_flag char(1);          --客户自送
 op_first_jrny_no varchar2(100);       --行程拆分第一段
 op_last_jrny_no varchar2(100);        --行程拆分最后一段
 op_route_id varchar2(32);             --线路ID
 op_refenence1 varchar2(100);
 op_cod_flag char(1);                  --是否需要收款标记
 op_unload_id varchar2(32);            --卸货点
 op_customer_id varchar2(32);          --客户
 op_customer_name varchar2(100);       --客户名称

 --分点部代码信息
 op_addr_id varchar2(32);
 op_addr_name varchar2(100);
 op_address VARCHAR2(300);
 op_cont_name varchar2(50);
 op_cont_tel varchar2(50);
 op_area_id varchar2(32);
 op_area_name varchar2(50);
 op_area_id2 varchar2(32);
 op_area_name2 varchar2(50);
 op_area_id3 varchar2(32);
 op_area_name3 varchar2(50);
 op_shpm_no varchar2(100);
 op_code varchar2(50);
 op_qnty NUMBER(18,8);
 op_gwgt NUMBER(18,8);
 op_vol NUMBER(18,8);
 op_nwgt NUMBER(18,8);
 op_qnty_each NUMBER(18,8);
 op_worth NUMBER(18,8);
 op_temperature1 varchar2(32);
 op_udf5 char(1);

 op_load_count number(4);
 op_unload_count number(4);

 --临时
 t_count number(4);
 t_count2 number(4);
 t_count3 number(4);
 t_addr_id varchar2(32);
 in_addr LST := LST();
 in_shpm LST := LST();
 t_msg varchar2(100);
 t_valid char(1);
 t_cur_loadid varchar2(32);
 t_cur_unloadid varchar2(32);

 in_load_id LST := LST();
 in_unload_id LST := LST();
 in_load_area LST := LST();
 in_unload_area LST := LST();

 tmp_result varchar2(200);

 t_result varchar2(1024);
 unld_four_query_flag char(1);

 pos number(4);
CURSOR addr IS
  select addr_id,area_id from bas_route_detail where route_id = (select id from bas_route_head where start_area_id2 = op_load_area_id2 and end_area_id2 = op_area_id2 and trans_srvc_id = RDPG_STAT.SDRYLINE AND ENABLE_FLAG = 'Y' and nvl(points_flag,'N') = 'N' AND ROWNUM <= 1) and addr_id is not null;

CURSOR item IS
  select load_id,unload_id,load_area_id2,unload_area_id2,load_name,unload_name from trans_order_item where odr_no = in_odr_no order by odr_row asc;

begin
    output_result:='00';
    t_valid := 'Y';
    unld_four_query_flag := 'N';
    t_cur_loadid := ' ';
    t_cur_unloadid := ' ';
    pos := 1;
    op_load_count := 1;
    op_unload_count := 1;
    op_udf5 := 'N';
    --汇总托运单信息
    ORDER_QTY_COUNT(in_odr_no, t_msg);
    --fanglm 判断托运单是否已经被确认
    select status,biz_typ,load_area_id2,load_area_name2,load_id,unload_id,unload_name,unload_address,unload_area_id,unload_area_id2,unload_area_id3,cod_flag,customer_name,
      unload_area_name,unload_area_name2,unload_area_name3,unload_contact,unload_tel,trans_srvc_id,trans_srvc_id_name,slf_pickup_flag,slf_deliver_flag,refenence1,customer_id,load_code
    into op_status,op_biz_type,op_load_area_id2,op_load_area_name2,op_load_id,op_addr_id,op_addr_name,op_address,op_area_id,op_area_id2,op_area_id3,op_cod_flag,op_customer_name,
      op_area_name,op_area_name2,op_area_name3,op_cont_name,op_cont_tel,op_trans_srvc_id,op_trans_srvc_name,op_slf_pickup_flag,op_slf_deliver_flag,op_refenence1,op_customer_id,op_load_code
    from trans_order_header where odr_no=in_odr_no;
    if op_customer_id is null then
       output_result := '01客户不能为空!';
       return;
    end if;
    if op_status > 10 then
       output_result := '01' || in_odr_no || '已审核!';
       return;
    end if;

    op_unload_area_id2 := op_area_id2;
    op_unload_id := op_addr_id;

    select count(1) into t_count from bas_address where addr_typ = RDPG_STAT.RDC and enable_flag = 'Y' and area_id2 = op_unload_area_id2;  --生成第二段中转点
    if t_count < 1 then
        unld_four_query_flag := 'Y';
    end if;

    --非B2B业务
    if op_biz_type != RDPG_STAT.B2B then
        --获取分点部代码对应的地址信息
        if op_biz_type = RDPG_STAT.B2C or op_biz_type = RDPG_STAT.SHOP then
            if op_load_area_id2 = op_area_id2 then  --B2C并且同城
                op_trans_srvc_id := RDPG_STAT.SDISPATCH;
                op_trans_srvc_name := RDPG_STAT.SDISPATCH_NAME;
            else
                op_trans_srvc_id := RDPG_STAT.SDRYLINE;
                op_trans_srvc_name := RDPG_STAT.SDRYLINE_NAME;
            end if;
        end if;
        if op_biz_type = RDPG_STAT.LD then  --零担
            if unld_four_query_flag = 'Y' then
                op_trans_srvc_id := RDPG_STAT.SDRYLINE;
                op_trans_srvc_name := RDPG_STAT.SDRYLINE_NAME;
            else
                op_trans_srvc_id := RDPG_STAT.SDELIVERY;
                op_trans_srvc_name := RDPG_STAT.SDELIVERY_NAME;
            end if;
        end if;
    end if;

    FOR h IN item LOOP
      if t_cur_loadid <> h.load_id or t_cur_unloadid <> h.unload_id then
          in_load_id.extend();
          in_load_id(pos) := h.load_id;
          in_unload_id.extend();
          in_unload_id(pos) := h.unload_id;
          in_load_area.extend();
          in_load_area(pos) := h.load_area_id2;
          in_unload_area.extend();
          in_unload_area(pos) := h.unload_area_id2;
          pos := pos + 1;

          t_cur_loadid := h.load_id;
          t_cur_unloadid := h.unload_id;
      end if;
      if op_udf3 is null or instr(op_udf3,h.load_name,1) < 1 then
          if op_udf3 is null then
              op_udf3 := h.load_name;
          else
              op_udf3 := op_udf3 || '&' || h.load_name;
          end if;
      end if;
      if op_udf4 is null or instr(op_udf4,h.unload_name,1) < 1 then
          if op_udf4 is null then
              op_udf4 := h.unload_name;
          else
              op_udf4 := op_udf4 || '&' || h.unload_name;
          end if;
      end if;
    end loop;

    op_udf3 := op_udf3 || '-' || op_udf4;
    if length(op_udf3) > 1000 then
        op_udf3 := substr(op_udf3,0,1000);
    end if;

    /*if in_load_id.count = 0 then
        output_result := '01该托运单没有货品明细，不能执行【确认】!';
        return;
    end if;*/

    --1.获取作业单默认执行机构和供应商e
    begin
        select t1.id,t1.org_cname into op_exec_org_id,op_exec_org_name from bas_org_area t,bas_org t1 where t.org_id = t1.id and t.area_id = op_load_area_id2 and rownum <= 1;
        exception when NO_DATA_FOUND THEN
            begin
                select t.id,t.org_cname into op_exec_org_id,op_exec_org_name from bas_org t,bas_address t1 where t.id = t1.exec_org_id and t1.id = op_load_id and rownum <= 1;
                exception when NO_DATA_FOUND then
                    output_result := '01未找到[' || op_load_area_name2 || ']所属组织机构信息!';
                    return;
            end;
    end;

    begin
        select id,suplr_cname into op_suplr_id,op_suplr_name from bas_supplier where id = (select suplr_id from bas_supplier_org where org_id = op_exec_org_id and default_flag = 'Y' and rownum <= 1);
        exception when NO_DATA_FOUND then
            output_result := '00';
    end;

    if op_unload_id is null then
        --自动收集收货地址
        insert into bas_address(id,addr_code,customer_id,customer_name,load_flag,transfer_flag,recv_flag,area_id,area_name,addr_name,address,
          cont_name,cont_tel,addr_typ,create_org_id,exec_org_id,enable_flag,addtime,addwho,area_id2,area_name2,area_id3,area_name3)
        values(sys_guid(),F_ZJM(op_address),op_customer_id,op_customer_name,'N','N','Y',op_area_id,op_area_name,op_address,op_address,
          op_cont_name,op_cont_tel,'9E0057747CC24A4BB998749752D2C42E',op_exec_org_id,op_exec_org_id,'Y',sysdate,in_user_id,op_area_id2,op_area_name2,op_area_id3,op_area_name3);
    end if;

    if in_load_id.count = 1 then

        select max(temperature1),SUM(VOL),SUM(G_WGT) into op_temperature1,op_vol,op_gwgt from TRANS_ORDER_ITEM WHERE ODR_NO = in_odr_no;

        if op_vol > 0 and op_gwgt > 0 and op_vol/op_gwgt > 3 then
            op_udf5 := 'Y'; --抛货
        end if;
        --2生成作业单头
        insert into TRANS_SHIPMENT_HEADER(
        ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,SENDABLE_FLAG,SPLIT_FLAG,SPLIT_REASON_CODE,SPLIT_REASON,ADDWHO,ADDTIME,
        CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,TRANS_SRVC_ID_NAME,ODR_TYP,ASSIGN_STAT,ASSIGN_TIME,
        ODR_TIME,FROM_LOAD_TIME,PRE_LOAD_TIME,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,PRE_POD_TIME,
        LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_REGION,UNLOAD_REGION,
        STATUS,STATUS_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,UDF5,UDF6,POD_FLAG,
        CREATE_ORG_ID,CREATE_ORG_ID_NAME,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,BIZ_CODE,LOAD_STAT,
        TRANS_UOM,TOT_QNTY,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,UNLOAD_ZIP,SOURCE_NO,
        PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,
        DRIVER,MOBILE,ABNOMAL_STAT,WHSE_ID,END_UNLOAD_ID,END_UNLOAD_NAME,END_UNLOAD_ADDRESS,END_CONTACT,
        END_TEL,BIZ_TYP,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_CODE,
        UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,
        UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,SIGN_ORG_ID,SIGN_ORG_ID_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME)
        select
        sys_guid(),ODR_NO,ODR_NO,ODR_NO,'Y','N',NULL,NULL,in_user_id,ADDTIME,
        CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,op_trans_srvc_id,op_trans_srvc_name,ODR_TYP,RDPG_STAT.ASSIGNED,sysdate,
        ODR_TIME,FROM_LOAD_TIME,PRE_LOAD_TIME,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,PRE_POD_TIME,
        LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_REGION,UNLOAD_REGION,
        RDPG_STAT.SHPM_CONFIRM,RDPG_STAT.SHPM_CONFIRM_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,op_temperature1,op_udf5,'N','Y',
        CREATE_ORG_ID,CREATE_ORG_ID_NAME,UGRT_GRD,SALES_MAN,ENABLE_FLAG,BTCH_NUM,op_udf3,'未装车',
        TRANS_UOM,TOT_QNTY,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,UNLOAD_ZIP,SOURCE_NO,
        PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,op_suplr_id,op_suplr_name,VEHICLE_TYP,PLATE_NO,
        DRIVER,MOBILE,ABNOMAL_STAT,WHSE_ID,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_NAME || UNLOAD_AREA_NAME2 || UNLOAD_AREA_NAME3 || UNLOAD_ADDRESS,UNLOAD_CONTACT,
        UNLOAD_TEL,BIZ_TYP,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_CODE,
        UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,
        UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,op_exec_org_id,op_exec_org_name,op_exec_org_id,op_exec_org_name
        FROM TRANS_ORDER_HEADER WHERE ODR_NO = in_odr_no;

        --3 生成作业单明细
        insert into TRANS_SHIPMENT_ITEM(
        ID,SHPM_NO,SHPM_ROW,ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,QNTY,VOL,VOL_UNIT,G_WGT,N_WGT,WGT_UNIT,PRICE,QNTY_EACH,
        WORTH,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,ODR_QNTY,
        LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,
        REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,NOTES,MIX_FLAG,TEMPERATURE1,SKU_CLS,ADDWHO,ADDTIME)
        select
        sys_guid(),ODR_NO,ODR_ROW,ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,QNTY,VOL,VOL_UNIT,G_WGT,N_WGT,WGT_UNIT,PRICE,QNTY_EACH,
        WORTH,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,QNTY,
        LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,
        REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,substr(NOTES,0,84),MIX_FLAG,TEMPERATURE1,SKU_CLS,in_user_id,sysdate
        from TRANS_ORDER_ITEM WHERE ODR_NO = in_odr_no;

        insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
          values(sys_guid(),RDPG_STAT.ODR_CONFIRM,RDPG_STAT.SHPM_NO,in_odr_no,'订单确认，生成作业单',sysdate,in_user_id);
    else
        select count(distinct load_id) into op_load_count from trans_order_item where odr_no = in_odr_no;
        select count(distinct unload_id) into op_unload_count from trans_order_item where odr_no = in_odr_no;
        for i in 1..in_load_id.count loop

            op_shpm_no := in_odr_no || '#' || to_char(i);
            t_cur_loadid := in_load_id(i);
            t_cur_unloadid := in_unload_id(i);

            select addr_code,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address into
              op_load_code,op_load_name,op_load_area_id,op_load_area_name,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,op_load_contact,op_load_tel,op_load_address
              from bas_address where id = t_cur_loadid;

            select addr_code,addr_name,area_id,area_name,area_id2,area_name2,area_id3,area_name3,cont_name,cont_tel,address into
              op_code,op_addr_name,op_area_id,op_area_name,op_area_id2,op_area_name2,op_area_id3,op_area_name3,op_cont_name,op_cont_tel,op_address
              from bas_address where id = t_cur_unloadid;

            select sum(QNTY),SUM(VOL),SUM(G_WGT),SUM(N_WGT),SUM(QNTY_EACH),SUM(WORTH),max(temperature1) into op_qnty,op_vol,op_gwgt,op_nwgt,op_qnty_each,op_worth,op_temperature1
              from TRANS_ORDER_ITEM WHERE ODR_NO = in_odr_no and load_id = t_cur_loadid and unload_id = t_cur_unloadid;

            if op_vol > 0 and op_gwgt > 0 and op_vol/op_gwgt > 3 then
                op_udf5 := 'Y'; --抛货
            end if;
            insert into TRANS_SHIPMENT_HEADER(
            ID,SHPM_NO,ODR_NO,PARN_SHPM_NO,SENDABLE_FLAG,SPLIT_FLAG,SPLIT_REASON_CODE,SPLIT_REASON,ADDWHO,ADDTIME,
            CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID,TRANS_SRVC_ID_NAME,ODR_TYP,ASSIGN_STAT,ASSIGN_TIME,
            ODR_TIME,FROM_LOAD_TIME,PRE_LOAD_TIME,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,PRE_POD_TIME,
            LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_REGION,UNLOAD_REGION,
            STATUS,STATUS_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,UDF5,UDF6,POD_FLAG,
            CREATE_ORG_ID,CREATE_ORG_ID_NAME,UGRT_GRD,SALES_MAN,ENALBLE_FLAG,BTCH_NUM,BIZ_CODE,LOAD_STAT,
            TRANS_UOM,TOT_QNTY,TOT_GROSS_W,TOT_VOL,TOT_NET_W,TOT_WORTH,TOT_QNTY_EACH,ROUTE_ID,UNLOAD_ZIP,SOURCE_NO,
            PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,
            DRIVER,MOBILE,ABNOMAL_STAT,WHSE_ID,END_UNLOAD_ID,END_UNLOAD_NAME,END_UNLOAD_ADDRESS,END_CONTACT,
            END_TEL,BIZ_TYP,EXEC_STAT,LOAD_ZIP,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_CODE,
            UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,
            UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,SIGN_ORG_ID,SIGN_ORG_ID_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME)
            select
            sys_guid(),op_shpm_no,ODR_NO,ODR_NO,'Y','N',NULL,NULL,in_user_id,ADDTIME,
            CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,op_trans_srvc_id,op_trans_srvc_name,ODR_TYP,RDPG_STAT.ASSIGNED,sysdate,
            ODR_TIME,FROM_LOAD_TIME,PRE_LOAD_TIME,FROM_UNLOAD_TIME,PRE_UNLOAD_TIME,PRE_POD_TIME,
            t_cur_loadid,op_load_name,op_load_area_id,op_load_area_name,op_load_address,op_load_contact,op_load_tel,LOAD_REGION,UNLOAD_REGION,
            RDPG_STAT.SHPM_CONFIRM,RDPG_STAT.SHPM_CONFIRM_NAME,BILL_TO,REFENENCE1,REFENENCE2,REFENENCE3,op_temperature1,op_udf5,'N','Y',
            CREATE_ORG_ID,CREATE_ORG_ID_NAME,UGRT_GRD,SALES_MAN,ENABLE_FLAG,BTCH_NUM,op_udf3,'未装车',
            TRANS_UOM,op_qnty,op_gwgt,op_vol,op_nwgt,op_worth,op_qnty_each,ROUTE_ID,UNLOAD_ZIP,SOURCE_NO,
            PRINT_FLAG,SHOW_SEQ,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,NOTES,op_suplr_id,op_suplr_name,VEHICLE_TYP,PLATE_NO,
            DRIVER,MOBILE,ABNOMAL_STAT,WHSE_ID,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_NAME || UNLOAD_AREA_NAME2 || UNLOAD_AREA_NAME3 || UNLOAD_ADDRESS,UNLOAD_CONTACT,
            UNLOAD_TEL,BIZ_TYP,EXEC_STAT,LOAD_ZIP,op_load_area_id2,op_load_area_name2,op_load_area_id3,op_load_area_name3,op_load_code,
            t_cur_unloadid,op_addr_name,op_area_id,op_area_name,op_address,op_cont_name,op_cont_tel,op_code,
            op_area_id2,op_area_name2,op_area_id3,op_area_name3,op_exec_org_id,op_exec_org_name,op_exec_org_id,op_exec_org_name
            FROM TRANS_ORDER_HEADER WHERE ODR_NO = in_odr_no;

            --3 生成作业单明细
            insert into TRANS_SHIPMENT_ITEM(
            ID,SHPM_NO,SHPM_ROW,ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,QNTY,VOL,VOL_UNIT,G_WGT,N_WGT,WGT_UNIT,PRICE,QNTY_EACH,
            WORTH,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,ODR_QNTY,
            LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,
            REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,NOTES,MIX_FLAG,TEMPERATURE1,SKU_CLS,ADDWHO,ADDTIME)
            select
            sys_guid(),op_shpm_no,ODR_ROW,ODR_NO,SKU_ID,SKU_NAME,SKU_ENAME,SKU_SPEC,PACK_ID,UOM,QNTY,VOL,VOL_UNIT,G_WGT,N_WGT,WGT_UNIT,PRICE,QNTY_EACH,
            WORTH,LD_QNTY,LD_VOL,LD_GWGT,LD_NWGT,LD_WORTH,UNLD_QNTY,UNLD_VOL,UNLD_GWGT,UNLD_NWGT,UNLD_WORTH,PACK_TYP,QNTY,
            LOT_ID,LOTATT01,LOTATT02,LOTATT03,LOTATT04,LOTATT05,LOTATT06,LOTATT07,LOTATT08,LOTATT09,LOTATT10,LOTATT11,LOTATT12,
            REFENENCE1,REFENENCE2,REFENENCE3,REFENENCE4,SKU,substr(NOTES,0,84),MIX_FLAG,TEMPERATURE1,SKU_CLS,in_user_id,sysdate
            from TRANS_ORDER_ITEM WHERE ODR_NO = in_odr_no and load_id = t_cur_loadid and unload_id = t_cur_unloadid;

            insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
              values(sys_guid(),RDPG_STAT.ODR_CONFIRM,RDPG_STAT.SHPM_NO,in_odr_no,'订单确认，生成作业单',sysdate,in_user_id);
        end loop;
    end if;

    --4 更新托运单状态
    update TRANS_ORDER_HEADER set STATUS = RDPG_STAT.SO_CONFIRM,STATUS_NAME = RDPG_STAT.SO_CONFIRM_NAME,RECE_STAT= '10'
      ,RECE_STAT_NAME = '未收款',BIZ_CODE = op_udf3,editwho = in_user_id,edittime = sysdate,audit_time = sysdate,LOAD_COUNT = op_load_count,UNLOAD_COUNT = op_unload_count
      WHERE ODR_NO = in_odr_no;

    --5 插入业务日志
    insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
          values(sys_guid(),RDPG_STAT.ODR_CONFIRM,RDPG_STAT.ODR_NO,in_odr_no,'订单确认，生成作业单',sysdate,in_user_id);

    --SF状态
    SP_SFSTATUS_LOG(op_biz_type,'','',in_odr_no,'020',in_user_id);
    --commit;
    if op_biz_type = RDPG_STAT.B2C then   --B2C
    begin
        for i in 1..in_load_id.count loop
            op_load_area_id2 := in_load_area(i);
            op_unload_area_id2 := in_unload_area(i);
            if op_load_area_id2 <> op_unload_area_id2 then  --跨区域，需要先拆分,先干线到目的仓库，再由仓库送到分点部
                --判断是否存在路由
                select count(1) into t_count from bas_route_head where nvl(points_flag,'N') = 'N' and start_area_id2 = op_load_area_id2 and end_area_id2 = op_unload_area_id2 and trans_srvc_id = RDPG_STAT.SDRYLINE AND ROWNUM <= 1;
                if t_count > 0 then
                    --按路由拆分
                    t_count2 := 0;
                    t_valid := 'Y';
                    for it in addr loop
                        select count(1) into t_count3 from bas_org_area t,bas_org t1 where t.org_id = t1.id and t.area_id = it.area_id and rownum <= 1;
                        if t_count3  < 1 then
                            t_valid := 'N';
                        end if;
                    end loop;

                    if t_valid = 'Y' then
                        for it in addr loop
                            in_addr.extend;
                            in_addr(t_count2+1) := it.addr_id;
                            t_count2 := t_count2 + 1;
                        end loop;
                    end if;
                    if t_valid = 'N' or in_addr.count < 1 then
                        --自动拆分
                        if unld_four_query_flag = 'N' then
                            select id into t_addr_id from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = op_unload_area_id2 and rownum <= 1;
                            in_addr := LST();
                            in_addr.extend;
                            in_addr(1) := t_addr_id;
                        else
                            begin
                                select addr_id into t_addr_id from bas_address_area where area_id = op_unload_area_id2 and rownum <= 1;
                                in_addr := LST();
                                in_addr.extend;
                                in_addr(1) := t_addr_id;
                            exception when no_data_found then
                                t_count2 := 0;
                            end;
                        end if;
                    else
                        --自动拆分
                        if unld_four_query_flag = 'N' then
                            select id into t_addr_id from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = op_unload_area_id2 and rownum <= 1;
                            in_addr.extend;
                            in_addr(t_count2+1) := t_addr_id;
                        else
                            begin
                                select addr_id into t_addr_id from bas_address_area where area_id = op_unload_area_id2 and rownum <= 1;
                                in_addr.extend;
                                in_addr(t_count2+1) := t_addr_id;
                            exception when no_data_found then
                                t_count2 := 0;
                            end;
                        end if;
                    end if;
                else
                    --自动拆分
                    if unld_four_query_flag = 'N' then
                        select id into t_addr_id from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = op_unload_area_id2 and rownum <= 1;
                        in_addr.extend;
                        in_addr(1) := t_addr_id;
                    else
                        begin
                            select addr_id into t_addr_id from bas_address_area where area_id = op_unload_area_id2 and rownum <= 1;
                            in_addr.extend;
                            in_addr(1) := t_addr_id;
                        exception when no_data_found then
                            t_result := '000';
                        end;
                    end if;
                end if;
                --执行自动拆分
                if in_addr.count > 0 then
                    in_shpm.extend;
                    in_shpm(1) := in_odr_no;
                    if unld_four_query_flag = 'N' then
                        SP_SHPM_JNRY_SPLIT(in_shpm, in_addr, in_user_id,output_result);
                    else
                        SP_SHPM_JNRY_SPLIT_FOUR(in_shpm, in_addr, in_user_id,output_result);
                    end if;
                end if;
            end if;
        end loop;
    end;
    elsif op_biz_type = RDPG_STAT.LD then   --零担
       for i in 1..in_load_id.count loop
            op_load_area_id2 := in_load_area(i);
            op_unload_area_id2 := in_unload_area(i);

           select count(1) into t_count from bas_address where addr_typ = RDPG_STAT.RDC and enable_flag = 'Y' and area_id2 = op_load_area_id2;  --生成第二段中转点
           if t_count > 0 then
               select t.id,t.org_cname into op_exec_org_id,op_exec_org_name from bas_org t,bas_address t1 where t.id = t1.exec_org_id and t1.id = t_addr_id and rownum <= 1;
               --获取第一段中转点
               in_addr.extend;
               in_addr(1) := t_addr_id;
               if op_load_area_id2 <> op_unload_area_id2 then  --跨区域，需要拆分成提货、干线和配送3个节点；如果有路由，则按路由拆分出若干个干线，没路由则拆分出一个起点城市到目的城市的干线，提货段和配送段方式相同

                    --判断是否存在路由
                    select count(1) into t_count from bas_route_head where nvl(points_flag,'N') = 'N' and start_area_id2 = op_load_area_id2 and end_area_id2 = op_unload_area_id2 and trans_srvc_id = RDPG_STAT.SDRYLINE and rownum <= 1;
                    if t_count > 0 then
                        --按路由拆分
                        t_count2 := 1;
                        t_valid := 'Y';
                        for it in addr loop
                            --select count(1) into t_count3 from bas_org_area t,bas_org t1 where t.org_id = t1.id and t.area_id = it.area_id and rownum <= 1;
                            select count(1) into t_count3 from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = it.area_id and rownum <= 1;
                            if t_count3  < 1 then
                                t_valid := 'N';
                            end if;
                        end loop;

                        if t_valid = 'Y' then
                            for it in addr loop
                                in_addr.extend;
                                in_addr(t_count2+1) := it.addr_id;
                                t_count2 := t_count2 + 1;
                            end loop;
                        end if;
                        if t_valid = 'N' or in_addr.count < 1 then
                            in_addr := LST();
                            in_addr.extend;
                            in_addr(1) := t_addr_id;
                            --自动拆分
                            if unld_four_query_flag = 'N' then
                                select id into t_addr_id from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = op_unload_area_id2 and rownum <= 1;
                                in_addr.extend;
                                in_addr(2) := t_addr_id;
                            else
                                begin
                                    select addr_id into t_addr_id from bas_address_area where area_id = op_unload_area_id2 and rownum <= 1;
                                    in_addr.extend;
                                    in_addr(2) := t_addr_id;
                                exception when no_data_found then
                                    t_count2:=0;
                                end;
                            end if;
                        else
                            --自动拆分
                            if unld_four_query_flag = 'N' then
                                select id into t_addr_id from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = op_unload_area_id2 and rownum <= 1;
                                in_addr.extend;
                                in_addr(t_count2+1) := t_addr_id;
                            else
                                begin
                                    select addr_id into t_addr_id from bas_address_area where area_id = op_unload_area_id2 and rownum <= 1;
                                    in_addr.extend;
                                    in_addr(t_count2+1) := t_addr_id;
                                exception when no_data_found then
                                    t_count2:=0;
                                end;
                            end if;
                        end if;
                    else
                        --自动拆分
                        if unld_four_query_flag = 'N' then
                            select id into t_addr_id from bas_address where addr_typ = RDPG_STAT.RDC and def_rdc = 'Y' and area_id2 = op_unload_area_id2 and rownum <= 1;
                            in_addr.extend;
                            in_addr(2) := t_addr_id;
                        else
                            begin
                                select addr_id into t_addr_id from bas_address_area where area_id = op_unload_area_id2 and rownum <= 1;
                                in_addr.extend;
                                in_addr(2) := t_addr_id;
                            exception when no_data_found then
                                t_result :='000';
                            end;
                        end if;
                    end if;
               end if;
               --执行自动拆分
               if in_addr.count > 0 then
                   in_shpm.extend;
                   in_shpm(1) := in_odr_no;

                   if unld_four_query_flag = 'N' then
                       SP_SHPM_JNRY_SPLIT(in_shpm, in_addr, in_user_id,output_result);
                   else
                       SP_SHPM_JNRY_SPLIT_FOUR(in_shpm, in_addr, in_user_id,output_result);
                   end if;

                   select first_jrny_no,last_jrny_no into op_first_jrny_no,op_last_jrny_no from trans_shipment_header where parn_shpm_no <> shpm_no and parn_shpm_no = in_odr_no and rownum <= 1;
                   --拆分完后判断是否有自送情况
                   if op_slf_deliver_flag = 'Y' then
                       update trans_shipment_header set status = RDPG_STAT.SHPM_LOAD,status_name = RDPG_STAT.SHPM_LOAD_NAME,DEPART_TIME = sysdate,LOAD_NO = 'X',LOAD_STAT = '已装车' where shpm_no = op_first_jrny_no;
                       update trans_shipment_item set  LD_QNTY = QNTY,LD_VOL = VOL ,LD_GWGT = G_WGT,LD_NWGT = N_WGT where shpm_no = op_first_jrny_no;
                   end if;
                   update trans_shipment_header set POD_FLAG = 'Y' where shpm_no = op_last_jrny_no;
               end if;
           end if;
        end loop;
    elsif op_biz_type = RDPG_STAT.SHOP then --店配
    begin
        for i in 1..in_load_id.count loop
            op_load_area_id2 := in_load_area(i);
            op_unload_area_id2 := in_unload_area(i);
            if op_load_area_id2 <> op_unload_area_id2 then  --跨区域，需要先拆分,先干线到目的仓库，再由仓库送到分点部
                --判断是否存在路由
                select count(1) into t_count from bas_route_head where nvl(points_flag,'N') = 'N' and start_area_id2 = op_load_area_id2 and end_area_id2 = op_unload_area_id2 and trans_srvc_id = RDPG_STAT.SDRYLINE AND ROWNUM <= 1;
                if t_count > 0 then
                    --按路由拆分
                    t_count2 := 0;
                    t_valid := 'Y';
                    for it in addr loop
                        --select count(1) into t_count3 from bas_org_area t,bas_org t1 where t.org_id = t1.id and t.area_id = it.area_id and rownum <= 1;
                        select count(1) into t_count3 from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = it.area_id and rownum <= 1;
                        if t_count3  < 1 then
                            t_valid := 'N';
                        end if;
                    end loop;

                    if t_valid = 'Y' then
                        for it in addr loop
                            in_addr.extend;
                            in_addr(t_count2+1) := it.addr_id;
                            t_count2 := t_count2 + 1;
                        end loop;
                    end if;

                    if t_valid = 'N' or in_addr.count < 1 then
                        --自动拆分
                        if unld_four_query_flag = 'N' then
                            select id into t_addr_id from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = op_unload_area_id2 and rownum <= 1;
                            in_addr := LST();
                            in_addr.extend;
                            in_addr(1) := t_addr_id;
                        else
                            begin
                                select addr_id into t_addr_id from bas_address_area where area_id = op_unload_area_id2 and rownum <= 1;
                                in_addr := LST();
                                in_addr.extend;
                                in_addr(1) := t_addr_id;
                            exception when no_data_found then
                                t_result := '000';
                            end;
                        end if;
                    end if;
                else
                    --自动拆分
                    if unld_four_query_flag = 'N' then
                        select id into t_addr_id from bas_address where addr_typ = RDPG_STAT.RDC and area_id2 = op_unload_area_id2 and rownum <= 1;
                        in_addr.extend;
                        in_addr(1) := t_addr_id;
                    else
                        begin
                            select addr_id into t_addr_id from bas_address_area where area_id = op_unload_area_id2 and rownum <= 1;
                            in_addr.extend;
                            in_addr(1) := t_addr_id;
                        exception when no_data_found then
                            t_result := '000';
                        end;
                    end if;

                end if;
                --执行自动拆分
                if in_addr.count > 0 then
                    in_shpm.extend;
                    in_shpm(1) := in_odr_no;
                    if unld_four_query_flag = 'N' then
                        SP_SHPM_JNRY_SPLIT(in_shpm, in_addr, in_user_id,output_result);
                    else
                        SP_SHPM_JNRY_SPLIT_FOUR(in_shpm, in_addr, in_user_id,output_result);
                    end if;

                    select first_jrny_no,last_jrny_no into op_first_jrny_no,op_last_jrny_no from trans_shipment_header where parn_shpm_no <> shpm_no and parn_shpm_no = in_odr_no and rownum <= 1;
                    --拆分完后判断是否有自送情况
                    if op_slf_deliver_flag = 'Y' then
                        update trans_shipment_header set status = RDPG_STAT.SHPM_LOAD,status_name = RDPG_STAT.SHPM_LOAD_NAME,DEPART_TIME = sysdate,LOAD_NO = 'X',LOAD_STAT = '已装车' where shpm_no = op_first_jrny_no;
                        update trans_shipment_item set  LD_QNTY = QNTY,LD_VOL = VOL ,LD_GWGT = G_WGT,LD_NWGT = N_WGT where shpm_no = op_first_jrny_no;
                    end if;
                    update trans_shipment_header set POD_FLAG = 'Y' where shpm_no = op_last_jrny_no;
                end if;
            else
                --拆分完后判断是否有自送情况
                if op_slf_deliver_flag = 'Y' then
                    update trans_shipment_header set status = RDPG_STAT.SHPM_LOAD,status_name = RDPG_STAT.SHPM_LOAD_NAME,DEPART_TIME = sysdate,LOAD_NO = 'X',LOAD_STAT = '已装车' where shpm_no = in_odr_no;
                    update trans_shipment_item set  LD_QNTY = QNTY,LD_VOL = VOL ,LD_GWGT = G_WGT,LD_NWGT = N_WGT where shpm_no = in_odr_no;
                end if;

                --同城匹配线路
                begin
                if op_unload_id is not null then
                    select count(1) into t_count from bas_route_head t,bas_route_detail t1 where t.id = t1.route_id and t1.addr_id = op_unload_id;
                    if t_count = 1 then
                        select t.id into op_route_id from bas_route_head t,bas_route_detail t1 where t.id = t1.route_id and t1.addr_id = op_unload_id and rownum <= 1;
                        update trans_order_header set route_id = op_route_id where odr_no = in_odr_no;
                        update trans_shipment_header set route_id = op_route_id where shpm_no = in_odr_no;
                    else
                       insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
                       values(sys_guid(),'ODR_CONFIRM','ODR_NO',in_odr_no,'店配订单匹配线路失败:卸货地[' || op_unload_id || ']未匹配到或匹配到多条线路!',sysdate,'INTERFACE');
                    end if;
                    /*if op_load_id is not null then
                        select nvl(mileage,0) into op_route_mile from bas_address_dist where addr_id1 = op_load_id and addr_id2 = op_unload_id;
                    end if;*/
                end if;
                /*exception when NO_DATA_FOUND then
                    update trans_order_header set route_id = op_route_id,route_mile = null where odr_no = in_odr_no;
                    insert into TRANS_TRANSACTION_LOG(id,ACTION_TYP,DOC_TYP,DOC_NO,NOTES,ADDTIME,ADDWHO)
                    values(sys_guid(),'ODR_CONFIRM','ODR_NO',in_odr_no,'店配订单获取地址间里程数失败:[' || op_load_id || '-->' || op_unload_id || ']未匹配到或匹配到多条记录!',sysdate,'INTERFACE');*/
                end;
            end if;
        end loop;
    end;
    end if;
    commit;

    --计算托运单审核时间
    begin
         sp_time_calculate('','ODR_NO','ODR_NO',in_odr_no,0,1,tmp_result);
    exception when others then
         t_result := '000';
    end;
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result:='01'|| in_odr_no || sqlerrm;
       return;
end;

/

alter table trans_bill_pay add init_flag CHAR(1) DEFAULT 'N';
alter table bms_bill_pay add init_flag CHAR(1) NULL;

create or replace trigger SYNC_BILL_PAY after insert or update or delete
on TRANS_BILL_PAY for each row
declare
    integrity_error exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
if inserting then
    insert into BMS_BILL_PAY(ID,DOC_NO,CHARGE_TYPE,FEE_NAME,FEE_BAS,BAS_VALUE,PRICE,PRE_FEE,DUE_FEE,DISCOUNT_RATE,DISCOUNT_FEE,PROTOCOL_NO,NOTES,ACCOUNT_STAT,ACCOUNT_TIME
    ,ACCOUNTER,AUDIT_STAT,AUDIT_TIME,AUDITOR,INVOICE_NO,SETTLE_NO,ADDTIME,ADDWHO,EDITTIME,EDITWHO,SERIAL_NUM,CUSTOM_ODR_NO,UNLOAD_AREA_NAME,UNLOAD_NAME,UNLOAD_ADDRESS
    ,ODR_TIME,UNLOAD_TIME,DISPATCH_TIME,DEPART_TIME,EXEC_ORG_ID,ROUTE_MILE,VEHICLE_TYPE,FEE_ID,LOAD_NO,RECE_STAT,RECE_TIME,RECER,SUPLR_ID,SUPLR_NAME,PAY_FEE,PAY_STAT
    ,ACT_PAY_TIME,PAYEE,PRE_PAY_TIME,CUSTOMER_ID,CUSTOMER_NAME,SOURCE_METHOD,TFF_NAME,TFF_ID,RUL_ID,ODR_NO,INIT_FLAG)
    values(:NEW.ID,:NEW.DOC_NO,:NEW.CHARGE_TYPE,:NEW.FEE_NAME,:NEW.FEE_BAS,:NEW.BAS_VALUE,:NEW.PRICE,:NEW.PRE_FEE,:NEW.DUE_FEE,:NEW.DISCOUNT_RATE,:NEW.DISCOUNT_FEE,:NEW.PROTOCOL_NO,:NEW.NOTES,:NEW.ACCOUNT_STAT,:NEW.ACCOUNT_TIME
    ,:NEW.ACCOUNTER,:NEW.AUDIT_STAT,:NEW.AUDIT_TIME,:NEW.AUDITOR,:NEW.INVOICE_NO,:NEW.SETTLE_NO,:NEW.ADDTIME,:NEW.ADDWHO,:NEW.EDITTIME,:NEW.EDITWHO,:NEW.SERIAL_NUM,:NEW.CUSTOM_ODR_NO,:NEW.UNLOAD_AREA_NAME,:NEW.UNLOAD_NAME,:NEW.UNLOAD_ADDRESS
    ,:NEW.ODR_TIME,:NEW.UNLOAD_TIME,:NEW.DISPATCH_TIME,:NEW.DEPART_TIME,:NEW.EXEC_ORG_ID,:NEW.ROUTE_MILE,:NEW.VEHICLE_TYPE,:NEW.FEE_ID,:NEW.LOAD_NO,:NEW.RECE_STAT,:NEW.RECE_TIME,:NEW.RECER,:NEW.SUPLR_ID,:NEW.SUPLR_NAME,:NEW.PAY_FEE,:NEW.PAY_STAT
    ,:NEW.ACT_PAY_TIME,:NEW.PAYEE,:NEW.PRE_PAY_TIME,:NEW.CUSTOMER_ID,:NEW.CUSTOMER_NAME,:NEW.SOURCE_METHOD,:NEW.TFF_NAME,:NEW.TFF_ID,:NEW.RUL_ID,:NEW.ODR_NO,:NEW.INIT_FLAG);
elsif updating then
    update BMS_BILL_PAY set DOC_NO=:NEW.DOC_NO,CHARGE_TYPE=:NEW.CHARGE_TYPE,FEE_NAME=:NEW.FEE_NAME,FEE_BAS=:NEW.FEE_BAS,BAS_VALUE=:NEW.BAS_VALUE
    ,PRICE=:NEW.PRICE,PRE_FEE=:NEW.PRE_FEE,DUE_FEE=:NEW.DUE_FEE,DISCOUNT_RATE=:NEW.DISCOUNT_RATE,DISCOUNT_FEE=:NEW.DISCOUNT_FEE,PROTOCOL_NO=:NEW.PROTOCOL_NO
    ,NOTES=:NEW.NOTES,ACCOUNT_STAT=:NEW.ACCOUNT_STAT,ACCOUNT_TIME=:NEW.ACCOUNT_TIME,ACCOUNTER=:NEW.ACCOUNTER,AUDIT_STAT=:NEW.AUDIT_STAT,AUDIT_TIME=:NEW.AUDIT_TIME
    ,AUDITOR=:NEW.AUDITOR,INVOICE_NO=:NEW.INVOICE_NO,SETTLE_NO=:NEW.SETTLE_NO,ADDTIME=:NEW.ADDTIME,ADDWHO=:NEW.ADDWHO,EDITTIME=:NEW.EDITTIME,EDITWHO=:NEW.EDITWHO
    ,SERIAL_NUM=:NEW.SERIAL_NUM,CUSTOM_ODR_NO=:NEW.CUSTOM_ODR_NO,UNLOAD_AREA_NAME=:NEW.UNLOAD_AREA_NAME,UNLOAD_NAME=:NEW.UNLOAD_NAME,UNLOAD_ADDRESS=:NEW.UNLOAD_ADDRESS
    ,ODR_TIME=:NEW.ODR_TIME,UNLOAD_TIME=:NEW.UNLOAD_TIME,DISPATCH_TIME=:NEW.DISPATCH_TIME,DEPART_TIME=:NEW.DEPART_TIME,EXEC_ORG_ID=:NEW.EXEC_ORG_ID,ROUTE_MILE=:NEW.ROUTE_MILE
    ,VEHICLE_TYPE=:NEW.VEHICLE_TYPE,FEE_ID=:NEW.FEE_ID,LOAD_NO=:NEW.LOAD_NO,RECE_STAT=:NEW.RECE_STAT,RECE_TIME=:NEW.RECE_TIME,RECER=:NEW.RECER,SUPLR_ID=:NEW.SUPLR_ID
    ,SUPLR_NAME=:NEW.SUPLR_NAME,PAY_FEE=:NEW.PAY_FEE,PAY_STAT=:NEW.PAY_STAT,ACT_PAY_TIME=:NEW.ACT_PAY_TIME,PAYEE=:NEW.PAYEE,PRE_PAY_TIME=:NEW.PRE_PAY_TIME,CUSTOMER_ID=:NEW.CUSTOMER_ID
    ,CUSTOMER_NAME=:NEW.CUSTOMER_NAME,SOURCE_METHOD=:NEW.SOURCE_METHOD,TFF_NAME=:NEW.TFF_NAME,TFF_ID=:NEW.TFF_ID,RUL_ID=:NEW.RUL_ID,ODR_NO=:NEW.ODR_NO,INIT_FLAG=:NEW.INIT_FLAG
    where id=:OLD.id;
elsif deleting then
    delete from BMS_BILL_PAY where id=:OLD.id;
end if;
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/

13	53F37E1A9D1E4C63A8D6785DACB4B26C	1200	费用调增	00DB1CA43D244461AF71A1A831A0405B	E43845662CE04B80995D3AE8FB41D11F	0.00	FYDZ	9	Y	2017/5/18 21:15:53	wpsadmin			94C318BD9B3F49348BAAC47E56F0D24F
16	38A1FBEB344D4D2AA3A4A2BA9C0C3778	1300	费用调减	00DB1CA43D244461AF71A1A831A0405B	E43845662CE04B80995D3AE8FB41D11F	0.00	FYDJ	10	Y	2017/5/18 21:16:12	wpsadmin	2017/5/19 9:53:04	wpsadmin	D38F878BAD7D4000B4C52B7F5A94368D
CREATE OR REPLACE PROCEDURE SP_SETT_SAV_PAY_FEE
(
in_id varchar2,
in_load_no varchar2,
in_doc_no varchar2,
in_fee_id varchar2,
in_fee_name varchar2,
in_fee_bas varchar2,
in_bas_value varchar2,
in_price varchar2,
in_pre_fee varchar2,
in_pay_fee varchar2,
in_pre_rece_time date,
in_notes varchar2,
in_flag varchar2,
in_user_id varchar2,
output_result out varchar2
)
IS
op_id varchar2(32);
t_count number(4);
sum_pre_fee number(18,8);
sum_bas_value number(18,8);
sum_price number(18,8);

sum_count number(4);

t_shpm_no varchar2(100);
tmp_sql varchar2(1024);

fm_bas_value number(18,8);
fm_price number(18,8);
fm_due_fee number(18,8);
op_add_amount number(18,8);
op_sub_amount number(18,8);

 CURSOR shpm  IS
   SELECT SHPM_NO FROM TRANS_SHIPMENT_HEADER WHERE  load_no =in_load_no;
begin
    output_result := '00';
    if in_flag = 'A' then
        select sys_guid() into op_id from dual;
        if in_doc_no is not null then
            select count(1) into t_count from trans_bill_pay where doc_no = in_doc_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
            if t_count > 0 then
                output_result := '01费用项目已存在';
                return;
            end if;
            insert into TRANS_BILL_PAY(ID,DOC_NO,CUSTOM_ODR_NO,CHARGE_TYPE,FEE_ID,FEE_NAME,FEE_BAS,BAS_VALUE,PRICE,PRE_FEE,DUE_FEE,PAY_FEE,
            ODR_TIME,UNLOAD_TIME,DEPART_TIME,NOTES,DISCOUNT_RATE,PRE_PAY_TIME,UNLOAD_AREA_NAME,UNLOAD_NAME,UNLOAD_ADDRESS,EXEC_ORG_ID,ROUTE_MILE,
            VEHICLE_TYPE,ADDTIME,ADDWHO,DISCOUNT_FEE,ACCOUNT_STAT,AUDIT_STAT,PAY_STAT,Customer_Id,Customer_Name,LOAD_NO,odr_no)
            SELECT sys_guid(),in_doc_no,custom_odr_no,'E43845662CE04B80995D3AE8FB41D11F',in_fee_id,in_fee_name,in_fee_bas,in_bas_value,in_price,0,in_pre_fee,in_pay_fee,
                odr_time,null,null,notes,'1',in_pre_rece_time,unload_area_name2,unload_name,unload_address,exec_org_id,route_mile,
                '',sysdate,in_user_id,in_pre_fee,'10','10','10',Customer_Id,Customer_Name,in_load_no,odr_no
            from trans_shipment_header where shpm_no = in_doc_no;
            commit;
            select count(1) into t_count from trans_bill_pay where doc_no = in_load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
            if t_count > 0 then
                 --汇总作业单费用
                 select sum(due_fee),sum(bas_value) into sum_pre_fee,sum_bas_value from trans_bill_pay where  load_no = in_load_no and doc_no != load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
                 if sum_bas_value = 0 then
                     sum_bas_value := 1;
                     sum_price := sum_pre_fee;
                 else
                     sum_price := sum_pre_fee/sum_bas_value;
                 end if;
                 update TRANS_BILL_PAY set DUE_FEE = sum_pre_fee, BAS_VALUE = sum_bas_value,PRICE = sum_price where doc_no = in_load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
            else
                insert into TRANS_BILL_PAY(ID,DOC_NO,CUSTOM_ODR_NO,CHARGE_TYPE,FEE_ID,FEE_NAME,FEE_BAS,BAS_VALUE,PRICE,PRE_FEE,DUE_FEE,PAY_FEE,
                ODR_TIME,UNLOAD_TIME,DEPART_TIME,NOTES,DISCOUNT_RATE,PRE_PAY_TIME,UNLOAD_AREA_NAME,UNLOAD_NAME,UNLOAD_ADDRESS,EXEC_ORG_ID,ROUTE_MILE,
                VEHICLE_TYPE,ADDTIME,ADDWHO,DISCOUNT_FEE,ACCOUNT_STAT,AUDIT_STAT,PAY_STAT,Customer_Id,Customer_Name,LOAD_NO)
                SELECT op_id,in_load_no,custom_odr_no,'E43845662CE04B80995D3AE8FB41D11F',in_fee_id,in_fee_name,in_fee_bas,in_bas_value,in_price,0,in_pre_fee,in_pay_fee,
                    odr_time,null,null,notes,'1',in_pre_rece_time,unload_area_name2,unload_name,unload_address,exec_org_id,route_mile,
                    '',sysdate,in_user_id,in_pre_fee,'10','10','10',Customer_Id,Customer_Name,in_load_no
                from trans_shipment_header where shpm_no = in_doc_no;
            end if;
        else
            select count(1) into t_count from trans_bill_pay where doc_no = in_load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
            if t_count > 0 then
                output_result := '01费用项目已存在';
                return;
            end if;
            insert into TRANS_BILL_PAY(ID,DOC_NO,CUSTOM_ODR_NO,CHARGE_TYPE,FEE_ID,FEE_NAME,FEE_BAS,BAS_VALUE,PRICE,PRE_FEE,DUE_FEE,PAY_FEE,
            ODR_TIME,UNLOAD_TIME,DEPART_TIME,NOTES,DISCOUNT_RATE,PRE_PAY_TIME,UNLOAD_AREA_NAME,UNLOAD_NAME,UNLOAD_ADDRESS,EXEC_ORG_ID,ROUTE_MILE,
            VEHICLE_TYPE,ADDTIME,ADDWHO,DISCOUNT_FEE,ACCOUNT_STAT,AUDIT_STAT,PAY_STAT,Customer_Id,Customer_Name,LOAD_NO)
            SELECT op_id,in_load_no,custom_odr_no,'E43845662CE04B80995D3AE8FB41D11F',in_fee_id,in_fee_name,in_fee_bas,in_bas_value,in_price,0,in_pre_fee,in_pay_fee,
                odr_time,null,null,in_notes,'1',in_pre_rece_time,unload_area_name2,unload_name,unload_address,exec_org_id,route_mile,
                '',sysdate,in_user_id,in_pre_fee,'10','10','10',Customer_Id,Customer_Name,in_load_no
            from trans_shipment_header where load_no = in_load_no and rownum <= 1;

            FOR h IN shpm LOOP
                t_shpm_no := h.shpm_no;

                if in_fee_bas = 'VEHICLE' then
                    select count(1) into sum_count from trans_shipment_header where load_no = in_load_no;
                    sum_bas_value := 1;
                    sum_pre_fee := in_pre_fee/sum_count;
                    sum_price := sum_pre_fee;
                else
                    sum_price := in_price;
                    tmp_sql := 'select ' || in_fee_bas || ' from trans_shipment_header where shpm_no = ''' || t_shpm_no || '''';
                    execute   immediate tmp_sql into sum_bas_value;
                    sum_pre_fee := sum_price * sum_bas_value;
                end if;

                insert into TRANS_BILL_PAY(ID,DOC_NO,CUSTOM_ODR_NO,CHARGE_TYPE,FEE_ID,FEE_NAME,FEE_BAS,BAS_VALUE,PRICE,PRE_FEE,DUE_FEE,PAY_FEE,
                ODR_TIME,UNLOAD_TIME,DEPART_TIME,NOTES,DISCOUNT_RATE,PRE_PAY_TIME,UNLOAD_AREA_NAME,UNLOAD_NAME,UNLOAD_ADDRESS,EXEC_ORG_ID,ROUTE_MILE,
                VEHICLE_TYPE,ADDTIME,ADDWHO,DISCOUNT_FEE,ACCOUNT_STAT,AUDIT_STAT,PAY_STAT,Customer_Id,Customer_Name,LOAD_NO,ODR_NO)
                SELECT sys_guid(),t_shpm_no,custom_odr_no,'E43845662CE04B80995D3AE8FB41D11F',in_fee_id,in_fee_name,in_fee_bas,sum_bas_value,sum_price,0,sum_pre_fee,in_pay_fee,
                    odr_time,null,null,notes,'1',in_pre_rece_time,unload_area_name2,unload_name,unload_address,exec_org_id,route_mile,
                    '',sysdate,in_user_id,sum_pre_fee,'10','10','10',Customer_Id,Customer_Name,in_load_no,odr_no
                from trans_shipment_header where shpm_no = t_shpm_no;
            end loop;
        end if;
        insert into bill_modify_log(doc_no,operation_name,from_base,to_base,from_price,to_price,from_amount,to_amount,addtime,addwho,notes)
        values(in_load_no,'录入费用',0,in_bas_value,0,in_price,0,in_pre_fee,sysdate,in_user_id,'');

        output_result := output_result || op_id;

    elsif in_flag = 'M' then
        select bas_value,price,due_fee into fm_bas_value,fm_price,fm_due_fee from TRANS_BILL_PAY where id = in_id;

        update TRANS_BILL_PAY set FEE_ID = in_fee_id,FEE_NAME = in_fee_name,FEE_BAS = in_fee_bas,BAS_VALUE = in_bas_value,PRICE = in_price,DUE_FEE = in_pre_fee, PAY_FEE = in_pay_fee,
        EDITWHO = in_user_id,EDITTIME = sysdate, DISCOUNT_RATE = '1', DISCOUNT_FEE = in_pre_fee,ACCOUNT_STAT = '10',AUDIT_STAT = '10',PAY_STAT = '10',NOTES = in_notes,
        PRE_PAY_TIME = in_pre_rece_time where id = in_id;

        insert into bill_modify_log(doc_no,operation_name,from_base,to_base,from_price,to_price,from_amount,to_amount,addtime,addwho,notes)
        values(in_doc_no,'修改费用',fm_bas_value,in_bas_value,fm_price,in_price,fm_due_fee,in_pre_fee,sysdate,in_user_id,'');
        if in_doc_no = in_load_no then
            delete from TRANS_BILL_PAY where load_no = in_load_no and doc_no != load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
            FOR h IN shpm LOOP
                t_shpm_no := h.shpm_no;

                if in_fee_bas = 'VEHICLE' then
                    select count(1) into sum_count from trans_shipment_header where load_no = in_load_no;
                    sum_bas_value := 1;
                    sum_pre_fee := in_pre_fee/sum_count;
                    sum_price := sum_pre_fee;
                else
                    sum_price := in_price;
                    tmp_sql := 'select ' || in_fee_bas || ' from trans_shipment_header where shpm_no = ''' || t_shpm_no || '''';
                    execute   immediate tmp_sql into sum_bas_value;
                    sum_pre_fee := sum_price * sum_bas_value;
                end if;

                insert into TRANS_BILL_PAY(ID,DOC_NO,CUSTOM_ODR_NO,CHARGE_TYPE,FEE_ID,FEE_NAME,FEE_BAS,BAS_VALUE,PRICE,PRE_FEE,DUE_FEE,PAY_FEE,
                ODR_TIME,UNLOAD_TIME,DEPART_TIME,NOTES,DISCOUNT_RATE,PRE_PAY_TIME,UNLOAD_AREA_NAME,UNLOAD_NAME,UNLOAD_ADDRESS,EXEC_ORG_ID,ROUTE_MILE,
                VEHICLE_TYPE,ADDTIME,ADDWHO,DISCOUNT_FEE,ACCOUNT_STAT,AUDIT_STAT,PAY_STAT,Customer_Id,Customer_Name,LOAD_NO)
                SELECT sys_guid(),t_shpm_no,custom_odr_no,'E43845662CE04B80995D3AE8FB41D11F',in_fee_id,in_fee_name,in_fee_bas,sum_bas_value,sum_price,0,sum_pre_fee,in_pay_fee,
                    odr_time,null,null,notes,'1',in_pre_rece_time,unload_area_name2,unload_name,unload_address,exec_org_id,route_mile,
                    '',sysdate,in_user_id,in_pre_fee,'10','10','10',Customer_Id,Customer_Name,in_load_no
                from trans_shipment_header where shpm_no = t_shpm_no;
            end loop;
        else
            select sum(bas_value),sum(due_fee) into sum_bas_value,sum_pre_fee from trans_bill_pay where load_no = in_load_no and doc_no != load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
            sum_price := sum_pre_fee/sum_bas_value;
            update TRANS_BILL_PAY set BAS_VALUE = sum_bas_value,PRICE = sum_price,DUE_FEE = sum_pre_fee,
            EDITWHO = in_user_id,EDITTIME = sysdate where doc_no = in_load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
        end if;
    else
        if in_doc_no = in_load_no then
            select bas_value,price,due_fee into fm_bas_value,fm_price,fm_due_fee from TRANS_BILL_PAY where ID = in_id;

            delete from trans_bill_pay where LOAD_NO =in_load_no and FEE_ID = in_fee_id;

        else
            select bas_value,price,due_fee into fm_bas_value,fm_price,fm_due_fee from TRANS_BILL_PAY where ID = in_id;

            delete from trans_bill_pay where ID = in_id;

            select sum(bas_value),sum(due_fee) into sum_bas_value,sum_pre_fee from trans_bill_pay where load_no = in_load_no and doc_no != load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;
            sum_price := sum_pre_fee/sum_bas_value;
            update TRANS_BILL_PAY set BAS_VALUE = sum_bas_value,PRICE = sum_price,DUE_FEE = sum_pre_fee,
            EDITWHO = in_user_id,EDITTIME = sysdate where doc_no = in_load_no and CHARGE_TYPE = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_ID = in_fee_id;

        end if;

        insert into bill_modify_log(doc_no,operation_name,from_base,to_base,from_price,to_price,from_amount,to_amount,addtime,addwho,notes)
            values(in_doc_no,'删除费用',fm_bas_value,0,fm_price,0,fm_due_fee,0,sysdate,in_user_id,'');
    end if;
    
    select sum(nvl(T.Due_FEE,0)) into op_add_amount FROM TRANS_BILL_PAY T,TRANS_CHARGE_TYPE T1

    WHERE T.FEE_ID = T1.ID and t1.Fee_Item = '94C318BD9B3F49348BAAC47E56F0D24F' and t.doc_no = in_load_no;

    select sum(nvl(T.DUE_FEE,0)) into op_sub_amount FROM TRANS_BILL_PAY T,TRANS_CHARGE_TYPE T1
    WHERE T.FEE_ID = T1.ID and t1.Fee_Item = 'D38F878BAD7D4000B4C52B7F5A94368D' and t.doc_no = in_load_no;

    update TRANS_LOAD_HEADER set TOT_AMOUNT = nvl(op_add_amount,0) - nvl(op_sub_amount,0),AUDIT_STAT = '10' where LOAD_NO = in_load_no;
    
    --update BMS_LOAD_HEADER set TOT_AMOUNT = nvl(op_add_amount,0) - nvl(op_sub_amount,0) where LOAD_NO = in_load_no;
    
    commit;
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result := '01' || sqlerrm;
     return;
end;
/

--2017-5-19 yuanlei
alter table TRANS_LOAD_HEADER add INIT_FLAG CHAR(1) default 'N';

create or replace trigger SYNC_LOAD_HEADER after insert or update or delete
on TRANS_LOAD_HEADER for each row
declare
    integrity_error exception;
    errno            integer;
    errmsg           char(200);

begin
if inserting then
    insert into BMS_LOAD_HEADER(ID,LOAD_NO,STATUS,EXEC_ORG_ID,SUPLR_ID,SUPLR_NAME,VEHICLE_TYP_ID,PLATE_NO,DRIVER1,MOBILE1,
         DRVR_LIC_NUM,CONFIRMOR,CONFIRM_TIME,REF_NO,START_AREA_ID,START_AREA_NAME,END_AREA_ID,END_AREA_NAME,
         TRANS_SRVC_ID,START_ODOMETER,RETURN_ODOMETER,MILEAGE,EMPTY_MILEAGE,LADEN_MILEAGE,TRAILER_NO,DRIVER2,
         MOBILE2,PRE_PICKUP_TIME,PRE_DEPART_TIME,PICKUP_TIME,DEPART_TIME,ROUTE_ID,AP_TARRIF_ID,AUDIT_STAT,AUDIT_TIME,
         AUDITOR,PAY_STAT,PRE_PAY_TIME,PAY_TIME,PAYER,PRINT_FLAG,PRINT_TMS,ABNOMAL_STAT,SEAL_NO,TOT_QNTY,TOT_VOL,
         TOT_GROSS_W,TOT_WORTH,TRANS_UOM,CURRENT_ORG_ID,CURRENT_LOC,NOTES,ADDWHO,ADDTIME,EDITWHO,EDITTIME,
         AUDITED_FLAG,DONE_TIME,TOT_NET_W,ABNOMAL_NOTES,DISPATCH_STAT,SETT_MILE,TOT_QNTY_EACH,UDF1,UDF2,UDF3,UDF4,
         EXEC_ORG_ID_NAME,STATUS_NAME,DISPATCH_STAT_NAME,REMAIN_GROSS_W,REMAIN_VOL,MAX_GROSS_W,MAX_VOL,TEMPERATURE1,
         TEMPERATURE2,VEH_SIGN,LOAD_STAT,SIGN_ORG_ID,ACCOUNT_STAT,FEEAUDIT_STAT,ACCOUNT_TIME,FEEAUDIT_TIME,CHECK_FLAG,
         OP_PICKUP_TIME,GPS_NO1,GPS_NO2,TEMP_NO1,TEMP_NO2,LOAD_COUNT,UNLOAD_COUNT,PRE_UNLOAD_TIME,ARRIVE_WHSE_TIME,
         START_LOAD_TIME,END_LOAD_TIME,QUALIFIED_FLAG,AUDIT_NOTES,TOT_AMOUNT,Init_Flag)
    
    values(:NEW.ID,:NEW.LOAD_NO,:NEW.STATUS,:NEW.EXEC_ORG_ID,:NEW.SUPLR_ID,:NEW.SUPLR_NAME,:NEW.VEHICLE_TYP_ID,:NEW.PLATE_NO,:NEW.DRIVER1,:NEW.MOBILE1
    ,:NEW.DRVR_LIC_NUM,:NEW.CONFIRMOR,:NEW.CONFIRM_TIME,:NEW.REF_NO,:NEW.START_AREA_ID,:NEW.START_AREA_NAME,:NEW.END_AREA_ID,:NEW.END_AREA_NAME,
    :NEW.TRANS_SRVC_ID,:NEW.START_ODOMETER,:NEW.RETURN_ODOMETER,:NEW.MILEAGE,:NEW.EMPTY_MILEAGE,:NEW.LADEN_MILEAGE,:NEW.TRAILER_NO,:NEW.DRIVER2,
    :NEW.MOBILE2,:NEW.PRE_PICKUP_TIME,:NEW.PRE_DEPART_TIME,:NEW.PICKUP_TIME,:NEW.DEPART_TIME,:NEW.ROUTE_ID,:NEW.AP_TARRIF_ID,:NEW.AUDIT_STAT,:NEW.AUDIT_TIME,
    :NEW.AUDITOR,:NEW.PAY_STAT,:NEW.PRE_PAY_TIME,:NEW.PAY_TIME,:NEW.PAYER,:NEW.PRINT_FLAG,:NEW.PRINT_TMS,:NEW.ABNOMAL_STAT,:NEW.SEAL_NO,:NEW.TOT_QNTY,:NEW.TOT_VOL,
    :NEW.TOT_GROSS_W,:NEW.TOT_WORTH,:NEW.TRANS_UOM,:NEW.CURRENT_ORG_ID,:NEW.CURRENT_LOC,:NEW.NOTES,:NEW.ADDWHO,:NEW.ADDTIME,:NEW.EDITWHO,:NEW.EDITTIME,
    :NEW.AUDITED_FLAG,:NEW.DONE_TIME,:NEW.TOT_NET_W,:NEW.ABNOMAL_NOTES,:NEW.DISPATCH_STAT,:NEW.SETT_MILE,:NEW.TOT_QNTY_EACH,:NEW.UDF1,:NEW.UDF2,:NEW.UDF3,:NEW.UDF4,
    :NEW.EXEC_ORG_ID_NAME,:NEW.STATUS_NAME,:NEW.DISPATCH_STAT_NAME,:NEW.REMAIN_GROSS_W,:NEW.REMAIN_VOL,:NEW.MAX_GROSS_W,:NEW.MAX_VOL,:NEW.TEMPERATURE1,
    :NEW.TEMPERATURE2,:NEW.VEH_SIGN,:NEW.LOAD_STAT,:NEW.SIGN_ORG_ID,:NEW.ACCOUNT_STAT,:NEW.FEEAUDIT_STAT,:NEW.ACCOUNT_TIME,:NEW.FEEAUDIT_TIME,:NEW.CHECK_FLAG,
    :NEW.OP_PICKUP_TIME,:NEW.GPS_NO1,:NEW.GPS_NO2,:NEW.TEMP_NO1,:NEW.TEMP_NO2,:NEW.LOAD_COUNT,:NEW.UNLOAD_COUNT,:NEW.PRE_UNLOAD_TIME,:NEW.ARRIVE_WHSE_TIME,
    :NEW.START_LOAD_TIME,:NEW.END_LOAD_TIME,:NEW.QUALIFIED_FLAG,:NEW.AUDIT_NOTES,:NEW.TOT_AMOUNT,:NEW.INIT_FLAG);
elsif updating then
    update BMS_LOAD_HEADER set LOAD_NO=:NEW.LOAD_NO,STATUS=:NEW.STATUS,EXEC_ORG_ID=:NEW.EXEC_ORG_ID,SUPLR_ID=:NEW.SUPLR_ID,
    SUPLR_NAME=:NEW.SUPLR_NAME,VEHICLE_TYP_ID=:NEW.VEHICLE_TYP_ID,PLATE_NO=:NEW.PLATE_NO,DRIVER1=:NEW.DRIVER1,
    MOBILE1=:NEW.MOBILE1,DRVR_LIC_NUM=:NEW.DRVR_LIC_NUM,CONFIRMOR=:NEW.CONFIRMOR,CONFIRM_TIME=:NEW.CONFIRM_TIME,
    REF_NO=:NEW.REF_NO,START_AREA_ID=:NEW.START_AREA_ID,START_AREA_NAME=:NEW.START_AREA_NAME,END_AREA_ID=:NEW.END_AREA_ID,
    END_AREA_NAME=:NEW.END_AREA_NAME,TRANS_SRVC_ID=:NEW.TRANS_SRVC_ID,START_ODOMETER=:NEW.START_ODOMETER,
    RETURN_ODOMETER=:NEW.RETURN_ODOMETER,MILEAGE=:NEW.MILEAGE,EMPTY_MILEAGE=:NEW.EMPTY_MILEAGE,LADEN_MILEAGE=:NEW.LADEN_MILEAGE,
    TRAILER_NO=:NEW.TRAILER_NO,DRIVER2=:NEW.DRIVER2,MOBILE2=:NEW.MOBILE2,PRE_PICKUP_TIME=:NEW.PRE_PICKUP_TIME,
    PRE_DEPART_TIME=:NEW.PRE_DEPART_TIME,PICKUP_TIME=:NEW.PICKUP_TIME,DEPART_TIME=:NEW.DEPART_TIME,ROUTE_ID=:NEW.ROUTE_ID,
    AP_TARRIF_ID=:NEW.AP_TARRIF_ID,AUDIT_STAT=:NEW.AUDIT_STAT,AUDIT_TIME=:NEW.AUDIT_TIME,AUDITOR=:NEW.AUDITOR,
    PAY_STAT=:NEW.PAY_STAT,PRE_PAY_TIME=:NEW.PRE_PAY_TIME,PAY_TIME=:NEW.PAY_TIME,PAYER=:NEW.PAYER,PRINT_FLAG=:NEW.PRINT_FLAG,
    PRINT_TMS=:NEW.PRINT_TMS,ABNOMAL_STAT=:NEW.ABNOMAL_STAT,SEAL_NO=:NEW.SEAL_NO,TOT_QNTY=:NEW.TOT_QNTY,TOT_VOL=:NEW.TOT_VOL,
    TOT_GROSS_W=:NEW.TOT_GROSS_W,TOT_WORTH=:NEW.TOT_WORTH,TRANS_UOM=:NEW.TRANS_UOM,CURRENT_ORG_ID=:NEW.CURRENT_ORG_ID,
    CURRENT_LOC=:NEW.CURRENT_LOC,NOTES=:NEW.NOTES,ADDWHO=:NEW.ADDWHO,ADDTIME=:NEW.ADDTIME,EDITWHO=:NEW.EDITWHO,EDITTIME=:NEW.EDITTIME,
    AUDITED_FLAG=:NEW.AUDITED_FLAG,DONE_TIME=:NEW.DONE_TIME,TOT_NET_W=:NEW.TOT_NET_W,ABNOMAL_NOTES=:NEW.ABNOMAL_NOTES,
    DISPATCH_STAT=:NEW.DISPATCH_STAT,SETT_MILE=:NEW.SETT_MILE,TOT_QNTY_EACH=:NEW.TOT_QNTY_EACH,UDF1=:NEW.UDF1,
    UDF2=:NEW.UDF2,UDF3=:NEW.UDF3,UDF4=:NEW.UDF4,EXEC_ORG_ID_NAME=:NEW.EXEC_ORG_ID_NAME,STATUS_NAME=:NEW.STATUS_NAME,
    DISPATCH_STAT_NAME=:NEW.DISPATCH_STAT_NAME,REMAIN_GROSS_W=:NEW.REMAIN_GROSS_W,REMAIN_VOL=:NEW.REMAIN_VOL,
    MAX_GROSS_W=:NEW.MAX_GROSS_W,MAX_VOL=:NEW.MAX_VOL,TEMPERATURE1=:NEW.TEMPERATURE1,TEMPERATURE2=:NEW.TEMPERATURE2,
    VEH_SIGN=:NEW.VEH_SIGN,LOAD_STAT=:NEW.LOAD_STAT,SIGN_ORG_ID=:NEW.SIGN_ORG_ID,ACCOUNT_STAT=:NEW.ACCOUNT_STAT,
    FEEAUDIT_STAT=:NEW.FEEAUDIT_STAT,ACCOUNT_TIME=:NEW.ACCOUNT_TIME,FEEAUDIT_TIME=:NEW.FEEAUDIT_TIME,CHECK_FLAG=:NEW.CHECK_FLAG,
    OP_PICKUP_TIME=:NEW.OP_PICKUP_TIME,GPS_NO1=:NEW.GPS_NO1,GPS_NO2=:NEW.GPS_NO2,TEMP_NO1=:NEW.TEMP_NO1,TEMP_NO2=:NEW.TEMP_NO2,
    LOAD_COUNT=:NEW.LOAD_COUNT,UNLOAD_COUNT=:NEW.UNLOAD_COUNT,PRE_UNLOAD_TIME=:NEW.PRE_UNLOAD_TIME,
    ARRIVE_WHSE_TIME=:NEW.ARRIVE_WHSE_TIME,START_LOAD_TIME=:NEW.START_LOAD_TIME,END_LOAD_TIME=:NEW.END_LOAD_TIME,
    QUALIFIED_FLAG=:NEW.QUALIFIED_FLAG,AUDIT_NOTES=:NEW.AUDIT_NOTES,TOT_AMOUNT=:NEW.TOT_AMOUNT,INIT_FLAG=:NEW.INIT_FLAG
    where id=:OLD.id;
elsif deleting then
    delete from BMS_LOAD_HEADER where id=:OLD.id;
end if;
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/

CREATE OR REPLACE PROCEDURE SP_FEE_COMMIT
(
in_load_no IN LST,
in_user_id IN VARCHAR2,
out_put_result out varchar2
)
IS

op_load_no varchar2(100);
op_sum_amount number(18,8);
op_add_amount number(18,8);
op_sub_amount number(18,8);
op_audit_stat varchar2(20);
--in_load_no LST := LST();
BEGIN
    --in_load_no.extend;
    --in_load_no(1) := '170410101464_1';
    out_put_result := '00';
    for   i   in   1..in_load_no.count   loop
        op_load_no := in_load_no(i);
        select audit_stat into op_audit_stat from trans_load_header where load_no = op_load_no;
        if op_audit_stat = '20' then
            out_put_result := '01[' || op_load_no || ']已对帐单,无法提交审批!';
            return;
        end if;
    end loop;
    for   i   in   1..in_load_no.count   loop
        op_load_no := in_load_no(i);
        begin
            select sum(due_fee) into op_add_amount from  trans_bill_pay t,trans_charge_type t1
            where t.fee_id = t1.id and t1.fee_item = '94C318BD9B3F49348BAAC47E56F0D24F' and doc_no = op_load_no;
        exception when no_data_found then
            op_add_amount := 0;
        end;
        if op_add_amount is null then
            op_add_amount := 0;
        end if;
        begin
            select sum(due_fee) into op_sub_amount from  trans_bill_pay t,trans_charge_type t1
            where t.fee_id = t1.id and t1.fee_item = 'D38F878BAD7D4000B4C52B7F5A94368D' and doc_no = op_load_no;
        exception when no_data_found then
            op_sum_amount := 0;
        end;
        if op_sub_amount is null then
            op_sub_amount := 0;
        end if;
        if op_add_amount = 0 and op_sub_amount = 0 then
            out_put_result := '01[' || op_load_no || ']无费用信息,不需要提交审批!';
            return;
        end if;
        op_sum_amount := op_add_amount - op_sub_amount;

        update TRANS_LOAD_HEADER set AUDIT_STAT = '20',AUDIT_TIME = sysdate,AUDITOR = in_user_id,TOT_AMOUNT = op_sum_amount
        where LOAD_NO = op_load_no;

        update BMS_LOAD_HEADER set AUDIT_STAT = '20',AUDIT_TIME = sysdate,AUDITOR = in_user_id,TOT_AMOUNT = op_sum_amount
        where LOAD_NO = op_load_no;
    end loop;
    commit;
Exception
     WHEN OTHERS THEN
      out_put_result :='01'||sqlcode || sqlerrm; --失败标记
      ROLLBACK;
END;
/


--caijiante 2017-05-19
create or replace view v_bill_pay as
select t.load_no,t.doc_no,t.ID,t.suplr_id,t.suplr_name,t.CHARGE_TYPE,t.FEE_NAME,t.FEE_BAS,t.BAS_VALUE,round(t.PRICE,2) PRICE,
round(t.PRE_FEE,2) PRE_FEE,round(t.DUE_FEE,2) DUE_FEE,t.DISCOUNT_RATE,t.DISCOUNT_FEE,round(t.PAY_FEE,2) PAY_FEE,t.PROTOCOL_NO,t.NOTES,t.ACCOUNT_STAT,t.ACCOUNT_TIME,
t.ACCOUNTER,t.AUDIT_STAT,t.AUDIT_TIME,t.AUDITOR,t.INVOICE_NO,t.SETTLE_NO,t.ADDTIME,t.ADDWHO,t.EDITTIME,t.EDITWHO,t.SERIAL_NUM,t.CUSTOM_ODR_NO,t.UNLOAD_AREA_NAME,
t.UNLOAD_NAME,t.UNLOAD_ADDRESS,t.ODR_TIME,t.UNLOAD_TIME,t.DISPATCH_TIME,t.EXEC_ORG_ID,t.ROUTE_MILE,t.VEHICLE_TYPE,t.FEE_ID,t.RECE_STAT,t.RECE_TIME,t.RECER,
t1.name_c as CHARGE_TYPE_NAME,t2.name_c as ACCOUNT_STAT_NAME,t3.name_c as AUDIT_STAT_NAME,t5.name_c FEE_BAS_NAME
,'' as LOAD_AREA_CODE2,'' as UNLOAD_AREA_CODE2,t.odr_no,t.init_flag
,t.pay_stat,t8.name_c as pay_stat_name
from trans_bill_pay t
,bas_codes t1
,bas_codes t2
,bas_codes t3
,bas_codes t5
--,bas_address t6
--,bas_address t7
,bas_codes t8
where t.charge_type = t1.id(+) and t1.prop_code(+) = 'FEE_ATTR'
and t.account_stat = t2.code(+) and t2.prop_code(+) = 'ACCOUNT_STAT'
and t.audit_stat = t3.code(+) and t3.prop_code(+) = 'AUDIT_STAT'
and t.pay_stat=t8.code(+) and t8.prop_code(+)='PAY_STAT'
and t.fee_bas = t5.id(+) and t5.prop_code(+) = 'FEE_BASE';

UPDATE SYS_FUNC_PAGE SET SUBSYSTEM_TYPE = 'U' WHERE FUNCTION_ID LIKE 'P00_O10%' 
alter table BILL_PAY_INITDETAILS add UDF3 VARCHAR2(1000) null;

create or replace procedure BMS_TIMER_CREATEPAY
(
    IN_SUPLR_ID IN VARCHAR2,
    IN_FROM_DATE IN VARCHAR2,
    IN_TO_DATE IN VARCHAR2,
    out_put_result out varchar2
)

/**
 * YUANLEI 2013-05-15
 * 月初账单定时器
 */
is

op_load_no varchar2(100);
op_suplr_id varchar2(32);
op_notes varchar2(100);
op_depart_time varchar2(50);
op_unload_time varchar2(50);
op_vehicle_type_id  varchar2(32);
op_plate_no varchar2(50);
op_driver varchar2(50);
op_start_area_name VARCHAR2(50);
op_end_area_name VARCHAR2(50);
op_udf3 varchar2(1000);

op_add_amount number(18,8);
op_sub_amount number(18,8);
op_amount number(18,8);
op_tax_amount number(18,8);
op_sum_amount number(18,8);
op_sum_tax_amount number(18,8);

op_suplr_name varchar2(100);
op_tax number(4,2);
op_init_no varchar2(50);
op_belong_month varchar2(20);
op_customer_id varchar2(32);

tmp_index number(4);

output_result varchar2(1024);
cursor header is
     select load_no,suplr_id,suplr_name,vehicle_typ_id,plate_no,depart_time,DONE_TIME,driver1,start_area_name,end_area_name,udf3
     from bms_load_header where init_flag = 'N' and AUDIT_STAT= '30' and suplr_id = IN_SUPLR_ID
     AND depart_time >= to_date(IN_FROM_DATE,'YYYY-MM-DD') and depart_time <= to_date(IN_TO_DATE,'YYYY-M-DD');

begin
    out_put_result := '00';
    select to_char(add_months(sysdate,-1), 'YYYYMM') into op_belong_month from dual;

    begin
        select nvl(udf2,0) into op_tax from bas_supplier where id = IN_SUPLR_ID;
    exception when NO_DATA_FOUND THEN
        op_tax := 0;
    end;

    op_sum_amount := 0;
    op_amount := 0;
    tmp_index := 0;
    SP_GET_IDSEQ('PAY_INIT',op_init_no);

    for h in header loop
        op_load_no := h.load_no;
        op_suplr_id := h.suplr_id;
        op_suplr_name := h.suplr_name;
        op_depart_time := to_char(h.depart_time,'YYYY-MM-DD');
        op_unload_time := to_char(h.done_time,'YYYY-MM-DD');
        op_vehicle_type_id := h.vehicle_typ_id;
        op_plate_no := h.plate_no;
        op_driver := h.driver1;
        op_start_area_name := h.start_area_name;
        op_end_area_name := h.end_area_name;
        --op_notes := h.start_area_name || '->' || h.end_area_name;
        op_udf3 := h.udf3;
        op_add_amount := 0;
        op_sub_amount := 0;

        if tmp_index = 0 then
            select max(customer_id) into op_customer_id from trans_shipment_header where load_no = op_load_no;
        end if;
        select sum(nvl(T.DUE_FEE,0)) into op_add_amount FROM TRANS_BILL_PAY T,TRANS_CHARGE_TYPE T1
            WHERE T.FEE_ID = T1.ID and t1.Fee_Item = '94C318BD9B3F49348BAAC47E56F0D24F'
            and t.doc_no = op_load_no;

        select sum(nvl(T.DUE_FEE,0)) into op_sub_amount FROM TRANS_BILL_PAY T,TRANS_CHARGE_TYPE T1
            WHERE T.FEE_ID = T1.ID and t1.Fee_Item = 'D38F878BAD7D4000B4C52B7F5A94368D'
            and t.doc_no = op_load_no;
        if op_add_amount is null then
            op_add_amount := 0;
        end if;
        if op_sub_amount is null then
            op_sub_amount := 0;
        end if;
        op_amount := op_add_amount - op_sub_amount;
        op_tax_amount := round(op_amount*op_tax/(100+op_tax),2);

        if op_add_amount = 0 and op_sub_amount = 0 then
            insert into bill_action_log(id,doc_typ,log_typ,doc_no,notes,addtime,addwho)
            values(sys_guid(),'INIT_NO','PAY',op_init_no,op_load_no || '无费用!',sysdate,'SYSTEM');
        else
            insert into bill_pay_initdetails(id,init_no,load_no,suplr_id,suplr_name,plate_no,vehicle_typ_id,driver,load_date,unload_date,load_name,unload_name,tot_amount,init_amount,tax_amount,subtax_amount,addtime,addwho,notes,belong_month,from_init_no,adj_amount,customer_id,udf3)
            values(sys_guid(),op_init_no,op_load_no,op_suplr_id,op_suplr_name,op_plate_no,op_vehicle_type_id,op_driver,to_date(op_depart_time,'YYYY-MM-DD HH24:MI:SS'),to_date(op_unload_time,'YYYY-MM-DD HH24:MI:SS'),op_start_area_name
            ,op_end_area_name,op_amount,op_amount,op_tax_amount,op_amount-op_tax_amount,sysdate,'system',op_notes,op_belong_month,op_init_no,0,op_customer_id,op_udf3);

            update trans_load_header set init_flag = 'Y' where load_no = op_load_no;

            insert into bill_action_log(id,doc_typ,log_typ,doc_no,notes,addtime,addwho)
            values(sys_guid(),'INIT_NO','PAY',op_init_no,op_load_no,sysdate,'SYSTEM');
        end if;

        op_sum_amount := op_sum_amount + op_amount;
        tmp_index := tmp_index + 1;
    END LOOP;

    op_sum_tax_amount := round(op_sum_amount*op_tax/(100+op_tax),2);
    insert into bill_pay_initial(id,init_no,suplr_id,suplr_name,belong_month,initital_amount,tax_amount,subtax_amount,addtime,addwho,account_stat,init_amount,tax,adj_amount,confirm_amount,customer_id)
        values(sys_guid(),op_init_no,op_suplr_id,op_suplr_name,op_belong_month,op_sum_amount,op_sum_tax_amount,op_sum_amount-op_sum_tax_amount,sysdate,'system','10',op_sum_amount,op_tax,0,op_sum_amount,op_customer_id);
    commit;

    return;
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result:='01['|| op_load_no ||']'||sqlerrm;
       return;
end;

/

create table SYS_TIMER_LOG
(
  LOG_TYP  VARCHAR2(32),
  LOG_TIME DATE,
  NOTES    VARCHAR2(400)
)

create or replace procedure BMS_TIMER_INITPAY
/**
 * YUANLEI 2013-05-15
 * 月初账单定时器
 */
is

op_first_day varchar2(20);
op_last_day varchar2(20);
op_suplr_name varchar2(100);
   
output_result varchar2(1024);       
cursor header is 
     select distinct suplr_id,suplr_name
     from bms_load_header where init_flag = 'N' and AUDIT_STAT= '30';

begin
   
    output_result := '00';
    
    select to_char(trunc(add_months(sysdate,-1),'mm'),'YYYY-MM-DD'),to_char(last_day(add_months(sysdate,-1)),'YYYY-MM-DD')
    into op_first_day,op_last_day from dual;
    
    INSERT INTO SYS_TIMER_LOG(LOG_TYP,LOG_TIME,NOTES) VALUES('PAY',SYSDATE,'承运商月初账单启动!');
    for h in header loop
        op_suplr_name := h.suplr_name;
        INSERT INTO SYS_TIMER_LOG(LOG_TYP,LOG_TIME,NOTES) VALUES('PAY',SYSDATE,'开始生成【' || op_suplr_name || '】月初账单!');
        BMS_TIMER_CREATEPAY(h.suplr_id,op_first_day,op_last_day,output_result);
        commit;
    End loop;
    INSERT INTO SYS_TIMER_LOG(LOG_TYP,LOG_TIME,NOTES) VALUES('PAY',SYSDATE,'承运商月初账单结束!');
    commit;
                 
    EXCEPTION
       WHEN OTHERS THEN
           rollback;
           output_result:='01['|| op_suplr_name ||']'||sqlerrm;

       return;
end;

/

create table SYS_APPROVE_CUSTOMER
(
  HEAD_ID     VARCHAR2(32),
  CUSTOMER_ID VARCHAR2(32)
);

create table SYS_APPROVE_HEAD
(
  ID        VARCHAR2(32) not null,
  DOC_NO    VARCHAR2(100),
  DOC_DESCR VARCHAR2(200),
  ADDTIME   DATE not null,
  ADDWHO    VARCHAR2(20) not null,
  EDITTIME  DATE,
  EDITWHO   VARCHAR2(20)
);