package com.learning.doris;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.learning.doris.entity.Acc;
import com.learning.doris.service.AccService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DorisSpringbootDemoApplicationTests {

	@Autowired
	private AccService testService;

	@Test
	void contextLoads() {
	}

	@Test
	public void testSaveBatch(){
		long count =4339461L;
		List<Acc> list = new ArrayList<>();
	 	for(int i =0 ; i< 1000 ;i++){
			Acc acc = new Acc();
			acc.setId(count+i);
			acc.setpDate(new Date());
			acc.setCallid(SecureUtil.md5(acc.getId()+""));
			int randomIntInRange = RandomUtil.randomInt(1, 100);
			acc.setDuration(randomIntInRange);
			acc.setMsDuration(randomIntInRange*1000+100);
			acc.setCallerIn("20250613");
			acc.setCalleegateway("11");
			acc.setCalleeaccount("15");
			list.add(acc);
		}
	 	testService.saveAccs(list);
	}

	@Test
	public void initCsv(){
		// CSV文件路径
		final String csvFilePath = "/Users/admin/Downloads/large_data10.csv";

		// 创建CSV写入器（自动关闭资源）
		try (CsvWriter writer = new CsvWriter(csvFilePath, CharsetUtil.CHARSET_UTF_8)) {
			// 总数据量
			final int total = 1000000;
			// 批次大小
			final int batchSize = 1000;
			long count =1L;
			// 分批次生成并写入
			for (int batch = 0; batch < total / batchSize; batch++) {
				List<String[]> batchData = new ArrayList<>(batchSize);

				// 生成当前批次数据
				for (int i = 0; i < batchSize; i++) {
					int globalIndex = batch * batchSize + i;
					int randomIntInRange = RandomUtil.randomInt(1, 100);
					String[] row = new String[]{
						String.valueOf(count+globalIndex + 1),// ID
						DateUtil.format(new Date(), "yyyy-MM-dd"),
						SecureUtil.md5(count+globalIndex + 1+""),
						"0",
						"0",
						"test001",// 中文姓名
						"10",
						"15"
					};
					batchData.add(row);
				}

				// 批量写入CSV
				writer.write(batchData);

				// 进度输出（可选）
				if ((batch + 1) % 100 == 0) {
					System.out.printf("已写入 %d/%d 批次数据%n",
									  (batch + 1), total / batchSize);
				}
			}

			System.out.println("数据生成完成！文件路径：" + csvFilePath);
		}
	}
}
