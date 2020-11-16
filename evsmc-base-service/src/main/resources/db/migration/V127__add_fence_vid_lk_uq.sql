
DELETE FROM monitor_fence_veh_lk where electronic_fence_id in
(SELECT a.electronic_fence_id id from (SELECT MIN(id) min_id, count(*) n,
electronic_fence_id, vid from monitor_fence_veh_lk
GROUP BY electronic_fence_id, vid HAVING n > 1) a)
and id not in
(SELECT a.min_id from (SELECT MIN(id) min_id, count(*) n, electronic_fence_id, vid from monitor_fence_veh_lk
GROUP BY electronic_fence_id, vid HAVING n > 1) a);

ALTER TABLE `monitor_fence_veh_lk`
ADD UNIQUE INDEX `uq_electronic_fence_id_vid`(`electronic_fence_id`, `vid`) USING BTREE;