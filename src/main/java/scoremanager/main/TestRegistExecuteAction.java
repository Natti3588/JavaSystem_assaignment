package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistExecuteAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");
    School school = teacher.getSchool();

    Map<String, String> inputPoints = new HashMap<>();
    Map<String, String> errors = new HashMap<>();
    List<Test> saveList = new ArrayList<>();

    String entYearString = req.getParameter("f1");
    String classNumString = req.getParameter("f2");
    String subjectCd = req.getParameter("f3");
    String countString = req.getParameter("f4");

    int entYear = Integer.parseInt(entYearString);
    String classNum = classNumString;
    int count = Integer.parseInt(countString);

    SubjectDao subjectDao = new SubjectDao();
    Subject subject = subjectDao.get(subjectCd, school);


    TestDao testDao = new TestDao();
    List<Test> tests = testDao.filter(entYear, classNum, subject, count, school);

    for (Test test : tests) {
      String studentNo = test.getStudent().getNo();
      String pointStr = req.getParameter("point_" + studentNo);

      // 再表示用にポイントが書かれていれば、ポイントを表示
      inputPoints.put(studentNo, pointStr == null ? "" : pointStr);

      // 空欄だったらcontinue
      if (pointStr == null || pointStr.isEmpty()) {
        continue;
      }

      int point;
      try {
        point = Integer.parseInt(pointStr);
      } catch (NumberFormatException e) {
        errors.put(studentNo, "０～１００の値を入力してください");
        continue;
      }
      if (point < 0 || point > 100) {
        errors.put(studentNo, "０～１００の値を入力してください");
        continue;
      }

      test.setPoint(point);
      test.setSchool(school);
      test.setSubject(subject);
      test.setNo(count);
      saveList.add(test);
    }



    // エラーがあったら入力画面に戻す
    if (!errors.isEmpty()) {

      int year = LocalDate.now().getYear();
      List<Integer> entYearSet = new ArrayList<>();
      for (int i = year - 10; i <= year + 1; i++)
        entYearSet.add(i);

      List<String> numSet = new ArrayList<>();
      for (int i = 1; i <= 2; i++)
        numSet.add(String.valueOf(i));

      // エラーと得点
      req.setAttribute("errors", errors);
      req.setAttribute("input_points", inputPoints);

      req.setAttribute("tests", tests);
      req.setAttribute("subject", subject);
      req.setAttribute("num", count);
      req.setAttribute("ent_year_set", entYearSet);
      req.setAttribute("class_num_set", new ClassNumDao().filter(school));
      req.setAttribute("num_set", numSet);
      req.setAttribute("subjects", new SubjectDao().filter(school));
      req.setAttribute("f1", entYearString);
      req.setAttribute("f2", classNum);
      req.setAttribute("f3", subjectCd);
      req.setAttribute("f4", countString);


      req.getRequestDispatcher("test_regist.jsp").forward(req, res);
    }

    // saveメソッドが成功したらdoneにフォワード
    boolean isSuccess = testDao.save(tests);

    if (isSuccess) {
      req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
    }

  }

}
