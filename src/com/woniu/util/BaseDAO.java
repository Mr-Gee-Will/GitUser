package com.woniu.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;




public class BaseDAO<T> {
	Connection conn;
	ResultSet rs;
	PreparedStatement ps;
	
	public void update(String sql,Object[] obj){
		try {
			conn = JdbcUtils.getConn();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<obj.length;i++){
				ps.setObject(i+1, obj[i]);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JdbcUtils.closeConn(null, ps, conn);
		}
	}
	
	public List<T> select(String sql,Object[] objs,Class<T> c){
		List<T> list = new ArrayList<T>();
		try {
			conn = JdbcUtils.getConn();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<objs.length;i++){
				ps.setObject(i+1, objs[i]);
			}
			rs = ps.executeQuery();
			while(rs.next()){
				T t = c.newInstance();
				Method[] methods = c.getMethods();
				for(int i=0;i<methods.length;i++){
					Method m = methods[i];
					String mName = m.getName();
					if(mName.startsWith("set")){
						String fieldName = mName.substring(3);
						Class[] cs = m.getParameterTypes();
						if(cs[0]==Integer.class){
							m.invoke(t, rs.getInt(fieldName));
						}else if(cs[0]==String.class){
							m.invoke(t, rs.getString(fieldName));
						}else if(cs[0]==java.util.Date.class){
							m.invoke(t, rs.getDate(fieldName));
						}else if(cs[0]==Double.class){
							m.invoke(t, rs.getDouble(fieldName));
						}else if(cs[0]==Float.class){
							m.invoke(t, rs.getFloat(fieldName));
						}
					}
				}
				list.add(t);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JdbcUtils.closeConn(rs, ps, conn);
		}
		
		
		
		return list;
		
	}

}
