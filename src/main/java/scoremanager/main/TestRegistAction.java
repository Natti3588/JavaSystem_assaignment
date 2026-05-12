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

public class TestRegistAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");
    School school = teacher.getSchool();

    String entYearString = req.getParameter("f1");
    String classNum = req.getParameter("f2");
    String subjectCd = req.getParameter("f3");
    String countString = req.getParameter("f4");

    Map<String, String> errors = new HashMap<>();

    boolean isSubmitted = entYearString != null;

    boolean isValid = isSubmitted && !"0".equals(entYearString) && !"0".equals(classNum)
        && !"0".equals(subjectCd) && !"0".equals(countString);

    if (isValid) {
      int entYear = Integer.parseInt(entYearString);
      int count = Integer.parseInt(countString);

      // SubjectCdでSubjectを取得
      SubjectDao subjectDao = new SubjectDao();
      Subject subject = subjectDao.get(subjectCd, school);

      // TestDaoでフィルター
      TestDao testDao = new TestDao();
      List<Test> tests = testDao.filter(entYear, classNum, subject, count, school);

      req.setAttribute("tests", tests);
      req.setAttribute("subject", subject);
      req.setAttribute("num", count);
    } else if (isSubmitted) {
      errors.put("filter", "入学年度とクラスと科目と回数を選択してください");
    }

    // 選択肢
    int year = LocalDate.now().getYear();
    List<Integer> entYearSet = new ArrayList<>();
    for (int i = year - 10; i <= year + 1; i++)
      entYearSet.add(i);

    List<String> numSet = new ArrayList<>();
    for (int i = 1; i <= 2; i++)
      numSet.add(String.valueOf(i));

    List<Subject> subjects = new SubjectDao().filter(school);
    List<String> classNumSet = new ClassNumDao().filter(school);

    req.setAttribute("ent_year_set", entYearSet);
    req.setAttribute("class_num_set", classNumSet);
    req.setAttribute("num_set", numSet);
    req.setAttribute("subjects", subjects);
    req.setAttribute("errors", errors);
    req.setAttribute("f1", entYearString);
    req.setAttribute("f2", classNum);
    req.setAttribute("f3", subjectCd);
    req.setAttribute("f4", countString);

    req.getRequestDispatcher("test_regist.jsp").forward(req, res);
  }
}
