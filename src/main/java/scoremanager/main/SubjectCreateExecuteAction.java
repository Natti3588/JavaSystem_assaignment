package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectCreateExecuteAction extends Action{

	@Override
	public void execute(HttpServletRequest req,HttpServletResponse res)throws Exception{
		
		// ローカル変数の指定 1
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");

		String subject_cd = ""; 
		String subject_name = ""; 
		
		
		// 入力された科目番号を取得
		subject_cd = req.getParameter("cd");
		subject_name = req.getParameter("name");
		
//		科目コードの文字数をカウント
		int length = subject_cd.length();

		
//		科目インスタンスを作成
		Subject subject = new Subject();
//		SubjectDaoインスタンスの作成
		SubjectDao subjectDao = new SubjectDao();
//		エラーメッセージ
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

		// ビジネスロジック 4

//		科目コードが3文字じゃない場合
		if(length!=3){
			errors.put("1", "科目コードは3文字で入力してください");
			req.setAttribute("errors", errors);
			// 科目コードが重複している場合
		}else {
			if (subjectDao.get(subject_cd,teacher.getSchool()) != null) {
			errors.put("2", "科目コードが重複しています");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);

			} else {
//				// subjectに科目情報をセット
				subject.setCd(subject_cd);
				subject.setName(subject_name);
//
				subject.setSchool(teacher.getSchool());
//				// saveメソッドで情報を登録
				subjectDao.save(subject);
			}
		}
//		
		

		// JSPへフォワード 7
		if (errors.isEmpty()) {
			// エラーメッセージがない場合
			// 登録完了画面にフォワード
			req.getRequestDispatcher("subject_create_done.jsp").forward(req, res);
		} else { 
			// エラーメッセージがある場合
			//入力情報を属性にセット
			req.setAttribute("cd", subject_cd);
			req.setAttribute("name", subject_name);

			// 登録画面にフォワード
			req.getRequestDispatcher("SubjectCreate.action").forward(req, res);
		}
	}
}
