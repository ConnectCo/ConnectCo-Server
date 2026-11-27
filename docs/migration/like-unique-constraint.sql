-- 좋아요 테이블 유니크 제약 추가 전 실행: 같은 대상에 중복 행이 있으면 가장 최근 행만 남긴다.
-- 중복이 남아 있으면 제약 추가가 실패한다(ddl-auto: update도 제약을 만들지 못한다).

DELETE l FROM event_like l JOIN event_like k
  ON l.store_id = k.store_id AND l.event_id = k.event_id AND l.id < k.id;
DELETE l FROM coupon_like l JOIN coupon_like k
  ON l.organization_id = k.organization_id AND l.coupon_id = k.coupon_id AND l.id < k.id;
DELETE l FROM store_like l JOIN store_like k
  ON l.organization_id = k.organization_id AND l.store_id = k.store_id AND l.id < k.id;
DELETE l FROM organization_like l JOIN organization_like k
  ON l.organization_id = k.organization_id AND l.store_id = k.store_id AND l.id < k.id;

ALTER TABLE event_like        ADD CONSTRAINT uk_event_like        UNIQUE (store_id, event_id);
ALTER TABLE coupon_like       ADD CONSTRAINT uk_coupon_like       UNIQUE (organization_id, coupon_id);
ALTER TABLE store_like        ADD CONSTRAINT uk_store_like        UNIQUE (organization_id, store_id);
ALTER TABLE organization_like ADD CONSTRAINT uk_organization_like UNIQUE (organization_id, store_id);
