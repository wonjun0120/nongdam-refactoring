INSERT INTO region (name)
SELECT name
FROM (
         SELECT '서울' AS name
         UNION
         SELECT '경기/인천'
         UNION
         SELECT '강원'
         UNION
         SELECT '대전/세종'
         UNION
         SELECT '충남/충북'
         UNION
         SELECT '경남/경북'
         UNION
         SELECT '전남/전북'
         UNION
         SELECT '부산'
         UNION
         SELECT '제주'
     ) AS t
WHERE NOT EXISTS (SELECT 1 FROM region WHERE name = t.name);

INSERT INTO ingredient_category (name)
SELECT name
FROM (
         SELECT '과일' AS name
         UNION
         SELECT '고구마/감자/밤'
         UNION
         SELECT '쌀/옥수수/콩'
         UNION
         SELECT '고추/마늘/양파'
         UNION
         SELECT '나물'
         UNION
         SELECT '쌈채소'
         UNION
         SELECT '홍삼/인삼/새싹쌈'
         UNION
         SELECT '오이/파'
         UNION
         SELECT '배추/무'
         UNION
         SELECT '버섯'
         UNION
         SELECT '기타'
     ) AS t
WHERE NOT EXISTS (SELECT 1 FROM ingredient_category WHERE name = t.name);

INSERT INTO ingredient_ugly_reason (name)
SELECT name
FROM (
         SELECT '조금 작아요' AS name
         UNION
         SELECT '조금 커요'
         UNION
         SELECT '모양이 달라요'
         UNION
         SELECT '얼룩졌어요'
         UNION
         SELECT '상처났어요'
     ) AS t
WHERE NOT EXISTS (SELECT 1 FROM ingredient_ugly_reason WHERE name = t.name);

INSERT INTO farm_badge (name)
SELECT name
FROM (
         SELECT '원산지 인증' AS name
         UNION
         SELECT '당일포장 당일배송'
         UNION
         SELECT '빠른 답장'
     ) AS t
WHERE NOT EXISTS (SELECT 1 FROM farm_badge WHERE name = t.name);

