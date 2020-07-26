INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`)
VALUES
	(3, X'6D6963726F736572766963652D696E746567726174696F6E2D676174657761792D6465762E79616D6C', X'44454641554C545F47524F5550', X'7365727665723A0A2020706F72743A20343030300A737072696E673A0A202064617461736F757263653A0A2020202075726C3A206A6462633A6D7973716C3A2F2F3132372E302E302E312F676174657761793F75736553534C3D66616C736526636861726163746572456E636F64696E673D757466380A20202020757365726E616D653A2064656D6F0A2020202070617373776F72643A2064656D6F0A20202020647269766572436C6173734E616D653A20636F6D2E6D7973716C2E6A6462632E4472697665720A20202020747970653A20636F6D2E7A61787865722E68696B6172692E48696B61726944617461536F757263650A202068696B6172693A0A20202020636F6E6E656374696F6E2D74696D656F75743A2032303030300A202020206D696E696D756D2D69646C653A20310A202020206D6178696D756D2D706F6F6C2D73697A653A2032300A202072656469733A0A20202020686F73743A203132372E302E302E310A20202020706F72743A20363337390A2020202074696D656F75743A20353030300A202020206A656469733A0A202020202020706F6F6C3A0A20202020202020206D61782D6163746976653A20380A20202020202020206D61782D776169743A2031303030300A20202020202020206D61782D69646C653A20380A20202020202020206D696E2D69646C653A20300A2020636C6F75643A0A20202020676174657761793A0A202020202020646973636F766572793A0A20202020202020206C6F6361746F723A0A20202020202020202020656E61626C65643A20747275650A6D7962617469733A0A2020636F6E66696775726174696F6E3A0A202020206D6170556E64657273636F7265546F43616D656C436173653A20747275650A0A7061676568656C7065723A0A20206175746F4469616C6563743A20747275650A2020636C6F7365436F6E6E3A20747275650A202068656C7065724469616C6563743A206D7973716C0A2020726561736F6E61626C653A20747275650A0A6C6F6767696E673A0A20206C6576656C3A0A20202020636F6D2E75636F6D6D756E652E676174657761792E7265706F7369746F72793A207472616365', X'6537393361323833376562303065623761636139306535363030343462306336', '2019-05-08 00:18:53', '2020-07-26 00:33:00', NULL, X'303A303A303A303A303A303A303A31', X'', X'646576', X'', X'', X'', X'79616D6C', X''),
	(5, X'737072696E672D776974682D6E61636F732E70726F70657274696573', X'44454641554C545F47524F5550', X'68656C6C6F3D68656C6C6F2C776F726C6421', X'3033353264616562363435666361383731623038643465353464656635643930', '2019-07-15 22:54:31', '2019-07-15 22:54:31', NULL, X'303A303A303A303A303A303A303A31', X'', X'646576', NULL, NULL, NULL, X'70726F70657274696573', NULL),
	(6, X'6D6963726F736572766963652D696E746567726174696F6E2D617574682D6465762E79616D6C', X'44454641554C545F47524F5550', X'0A6C6F6767696E673A0A20206C6576656C3A0A20202020636F6D2E616C69626162612E6E61636F733A20696E666F0A', X'3230313337366362386163356432363839333363306261333636666362336561', '2020-07-26 00:27:24', '2020-07-26 04:15:42', NULL, X'303A303A303A303A303A303A303A31', X'', X'646576', X'', X'', X'', X'79616D6C', X''),
	(9, X'6D6963726F736572766963652D696E746567726174696F6E2D6170702D6465762E79616D6C', X'44454641554C545F47524F5550', X'2320E585B3E997AD657572656B61E6B3A8E5868C0A657572656B613A0A2020636C69656E743A0A20202020656E61626C65643A2066616C73650A0A6C6F6767696E673A0A20206C6576656C3A0A20202020636F6D2E616C69626162612E6E61636F733A20696E666F0A0A', X'3161316634643836626164616338383230616337376432363631653536666333', '2020-07-26 03:26:13', '2020-07-26 03:31:13', NULL, X'303A303A303A303A303A303A303A31', X'', X'646576', X'', X'', X'', X'79616D6C', X'');

INSERT INTO `tenant_info` (`id`, `kp`, `tenant_id`, `tenant_name`, `tenant_desc`, `create_source`, `gmt_create`, `gmt_modified`)
VALUES
	(1, X'31', X'36386461363731622D656639312D343765302D623564302D643361343538626437343361', X'646576', X'E6B58BE8AF95E78EAFE5A283', X'6E61636F73', 1557244049030, 1557244049030);

INSERT INTO `users` (`username`, `password`, `enabled`)
VALUES
	('demo', '$2a$10$pUlui0hqk.xmmA9MTFYZgekY66GVCpmZGBj/seSKhg3UzRnnBuiRm', 1);
