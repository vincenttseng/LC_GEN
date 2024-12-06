package com.vincent.coretest.yaml;

import static com.vincent.coretest.util.TextUtil.countLeadSpace;
import static com.vincent.coretest.util.TextUtil.removeEndingSemicolon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.util.TextUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PathDataStorage {
	protected final Logger logger = LoggerFactory.getLogger(PathDataStorage.class);

	Map<String, List<String>> pathContentMap;

	public void split(String filePath, List<String> rootList, List<HttpMethodDetailsVO> theHttpMethodDetailsVOList) {
		logger.info("rootList " + rootList);
		List<String> pathContentList = getPathsContent(filePath, rootList);
		List<PathContentVO> pathContentVOList = splitByUrlRequest(pathContentList);
		List<RESTFulDataVO> restFulDataVOList = spreadOutMethodContent(pathContentVOList);

		restFulDataVOList.stream().forEach(data -> {
			logger.info("#######################################");
			logger.info("!!!!!" + data.getPath() + "====" + data.getHttpMethod());
			data.getContent().stream().forEach(line -> {
				logger.info(line);
			});
		});

	}

	private List<RESTFulDataVO> spreadOutMethodContent(List<PathContentVO> pathContentVOList) {
		List<RESTFulDataVO> restFulDataVOList = new ArrayList<RESTFulDataVO>();

		for (PathContentVO aPathContentVO : pathContentVOList) {
			String theUrl = aPathContentVO.getUrl();
			List<String> theContentList = aPathContentVO.getContentList();

			String currentMethod = null;
			List<String> tmpContentList = new ArrayList<String>();
			for (String line : theContentList) {
				int spaceCnt = countLeadSpace(line);
				if (spaceCnt == 4) {
					if (tmpContentList != null) {
						List<String> contentList = new ArrayList<String>();
						contentList.addAll(tmpContentList);
						if (contentList == null || contentList.size() > 0) {
							RESTFulDataVO aRESTFulDataVO = new RESTFulDataVO(theUrl, currentMethod, contentList);
							restFulDataVOList.add(aRESTFulDataVO);
						}
					}
					tmpContentList.clear();
					currentMethod = TextUtil.getHttpMethodFromYamlWithComments(line.substring(4));
				} else {
					tmpContentList.add(line);
				}
			}
			if (currentMethod != null && tmpContentList.size() > 0) {
				RESTFulDataVO aRESTFulDataVO = new RESTFulDataVO(theUrl, currentMethod, tmpContentList);
				restFulDataVOList.add(aRESTFulDataVO);
			}
		}

		return restFulDataVOList;
	}

	private List<PathContentVO> splitByUrlRequest(List<String> pathContentList) {
		List<PathContentVO> pathContentVOList = new ArrayList<PathContentVO>();

		String currentUrl = null;
		List<String> tmpContentList = new ArrayList<String>();
		for (String line : pathContentList) {
			int spaceCnt = countLeadSpace(line);
			if (spaceCnt == 2) {
				if (currentUrl != null) {
					List<String> contentList = new ArrayList<String>();
					contentList.addAll(tmpContentList);
					PathContentVO pathContentVO = new PathContentVO(currentUrl, contentList);
					pathContentVOList.add(pathContentVO);
				}
				tmpContentList.clear();
				currentUrl = removeEndingSemicolon(line.substring(2));
			} else {
				tmpContentList.add(line);
			}
		}
		if (currentUrl != null && tmpContentList.size() > 0) {
			PathContentVO pathContentVO = new PathContentVO(currentUrl, tmpContentList);
			pathContentVOList.add(pathContentVO);
		}
		return pathContentVOList;
	}

	private List<String> getPathsContent(String filePath, List<String> rootList) {
		String startToken = null;
		String endToken = null;
		for (String tmp : rootList) {
			if ("paths".startsWith(tmp)) {
				startToken = tmp;
				continue;
			}
			if (startToken != null) {
				endToken = tmp;
				break;
			}
		}
		logger.info("from " + startToken + " to " + endToken);

		List<String> pathContentList = new ArrayList<String>();
		FileReader in = null;
		BufferedReader br = null;
		try {
			in = new FileReader(filePath);
			br = new BufferedReader(in);

			String line;
			boolean found = false;
			while ((line = br.readLine()) != null) {
				if (!found) {
					if (line.startsWith(startToken)) {
						found = true;
					}
				}
				if (!found) {
					continue;
				}

				if (line.startsWith(endToken)) {
					break;
				}
				pathContentList.add(line);
			}
		} catch (IOException e) {

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pathContentList;
	}

}
