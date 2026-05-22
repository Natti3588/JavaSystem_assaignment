package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassUpdateExecuteAction extends Action{
	  @Override
	  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		  
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher) session.getAttribute("user");
		// 1. パラメータの取得
	    String oldclass_num = req.getParameter("old"); // 変更前のクラス番号
	    String newclass_num = req.getParameter("num"); // 変更後のクラス番号
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

	    ClassNumDao classnumDao = new ClassNumDao();

	    // 2. ClassNumインスタンスの作成と値のセット
	    ClassNum classnum = new ClassNum();
	 // 前のクラスをインスタンスにセット
	    classnum.setClass_num(oldclass_num); 
	 // Schoolをセット
	    classnum.setSchool(teacher.getSchool()); 
	    
	    if (classnumDao.get(newclass_num,teacher.getSchool()) != null) {
			errors.put("2", "クラス番号が重複しています");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
		    req.getRequestDispatcher("class_update.jsp").forward(req, res);
		    return;}

	 // 3. 保存処理
	    
	    // classnum（Schoolセット済み）を第1引数に渡す
	    classnumDao.save(classnum, newclass_num);

	    // student_update_done.jspにフォワード
	    req.getRequestDispatcher("class_update_done.jsp").forward(req, res);
	  }

	}


