package scoremanager.main;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.Teacher;
import dao.ClassNumDao;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListAction extends Action {
  
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    
    // セッション情報をリクエストから取得
    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");
     
    // classNumDaoを宣言
    ClassNumDao classNumDao = new ClassNumDao();
    
    // 先生の学校に対応するクラス番号一覧を取得する
    List<String> classNum = classNumDao.filter(teacher.getSchool());
    
    // subjectDaoを宣言
    SubjectDao subjectDao = new SubjectDao();
    
    // 先生の学校の科目一覧を取得する
    List<bean.Subject> subjects = subjectDao.filter(teacher.getSchool());
    
    // 入学年度を保存するためのリストを作成
    List<Integer> entYearSet = new ArrayList<>();
    
    // 現在の年を取得する
    int year = Calendar.getInstance().get(Calendar.YEAR);
    
    // 10年前から来年までの年度をリストに追加する
    for (int i = year - 10; i <= year + 1; i++) {
      entYearSet.add(i);}
      
      // クラス番号・科目・入学年度のデータをJSPに渡す
      req.setAttribute("class_num_set", classNum);
      req.setAttribute("subject_set", subjects);
      req.setAttribute("ent_year_set", entYearSet);
      
      // test_list.jspへフォワード
      req.getRequestDispatcher("test_list.jsp").forward(req, res);
    }
  }
