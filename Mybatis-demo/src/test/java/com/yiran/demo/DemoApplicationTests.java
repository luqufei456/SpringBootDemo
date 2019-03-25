package com.yiran.demo;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yiran.demo.entity.Student;
import com.yiran.demo.entity.User;
import com.yiran.demo.mapper.StudentMapper;
import com.yiran.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(DemoApplicationTests.class);

	// TODO 这里会报错找不到，因为Mapper是运行时动态代理生成的实现，这里找不到classpath
	@Autowired
	private UserMapper userMapper;

	@Test
	public void test01() {
		final int row1 = userMapper.insert(new User("yiran", "yiranran"));
		log.info("[添加结果] - [{}]", row1);
		final int row2 = userMapper.insert(new User("yiran", "yiranranran"));
		log.info("[添加结果] - [{}]", row2);
		final int row3 = userMapper.insert(new User("yiran", "yiranranranran"));
		log.info("[添加结果] - [{}]", row3);
		final List<User> users = userMapper.findByUsername("yiran");
		log.info("[根据用户名查询] - [{}]", users);
	}

	@Autowired
	private StudentMapper studentMapper;

	@Test
	public void test02() {
		final Student yiran = new Student("yiran", "22");
		final Student miku = new Student("miku", "24");
		final Student yuebai = new Student("yuebai", "23");
		// TODO insertSelective，只插入对应字段，insert，插入所有字段，没有的话就插入null，可能性能上有一定优势
		studentMapper.insertSelective(yiran);
		log.info("[yiran回写主键] - [{}]", yiran.getId());
		studentMapper.insertSelective(miku);
		log.info("[miku回写主键] - [{}]", miku.getId());
		studentMapper.insertSelective(yuebai);
		log.info("[yuebai回写主键] - [{}]", yuebai.getId());
		final int count = studentMapper.countByStuName("yiran");
		log.info("[调用自己写的SQL] - [{}]", count);

		// TODO 模拟分页
		studentMapper.insertSelective(new Student("yiran1", "22"));
		studentMapper.insertSelective(new Student("yiran2", "22"));
		studentMapper.insertSelective(new Student("yiran3", "22"));
		studentMapper.insertSelective(new Student("yiran4", "22"));
		studentMapper.insertSelective(new Student("yiran5", "22"));
		studentMapper.insertSelective(new Student("yiran6", "22"));
		studentMapper.insertSelective(new Student("yiran7", "22"));
		studentMapper.insertSelective(new Student("yiran8", "22"));
		studentMapper.insertSelective(new Student("yiran9", "22"));
		studentMapper.insertSelective(new Student("yiran10", "22"));

		// TODO 分页 + 排序 this.studentMapper.selectAll() 这一句就是我们需要写的查询，有了这两款插件无缝切换各种数据库
		//() -> {} 这种形式是传的一个function定义进去了，() 你可以理解为new T()
		/*ISelect iSelect = new ISelect() {
			@Override
			public void doSelect() {
				studentMapper.selectAll();
			}
		};*/
		final PageInfo<Object> pageInfo = PageHelper.startPage(1,10).setOrderBy("id desc")
				.doSelectPageInfo(() -> this.studentMapper.selectAll());
		log.info("[lambda写法] - [分页信息] - [{}]", pageInfo.toString());

		PageHelper.startPage(1, 10).setOrderBy("id desc");
		final PageInfo<Student> pageInfo1 = new PageInfo<>(this.studentMapper.selectAll());
		log.info("[普通写法] - [{}]", pageInfo1.toString());
	}
}
