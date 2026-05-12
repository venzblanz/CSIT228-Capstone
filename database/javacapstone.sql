-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 12, 2026 at 09:42 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `javacapstone`
--

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `patient_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(20) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `profile_picture` longblob DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`patient_id`, `user_id`, `full_name`, `mobile_number`, `birthday`, `gender`, `address`, `profile_picture`, `created_at`) VALUES
(1, 1, 'Juner John Caimor', '912345678', '1910-10-07', 'Female', 'sa among house', 0xffd8ffe000104a46494600010100000100010000ffdb00840009060712131215131313151615171715171817171517171515171615161715151717181d2820181a251d151521312125292b2e2e2e171f3338332d37282d2e2b010a0a0a0e0d0e1a10101a2d1d1d1d2d2d2d2d2d2b2d2d2d2d2d2b2d2d2d2d2d2d2d2d2d2d2d2d2d2b2b2d2d2d2d2d2b2d2d2d2b2d2d2d2d2d2b2d2d2d2d2d2b37ffc000110800fa00ca03012200021101031101ffc4001c0000020301010101000000000000000000040502030601070008ffc400441000010302030505050507030109000000010002110304052131124151617106228191b1133242a1f0145262c1d11523337292b2e10782f124163443536383a2c2d2ffc4001a010003010101010000000000000000000001020300040506ffc4002411010100020301000202020300000000000001021112213103415104221361052332ffda000c03010002110311003f00c76c774741e881aadcca654c643a0f441dcb3bcbccdf6ef9003cafa9d574186cc7d78a9d4a6bb6e4e80782a4ad56da5ccb660ccc40e28c65d169cd996f8734e5d25116b81d522401c6011282bba4fa6ed9764781449699d28397c2edfc38156e0ef34ee6983f7834ff00bb2fcd0b86f79b1bf773e215d541963c6ad227a032d3e111e4834693b5b4269bb9b1c16170aa83d8d3921b996c6c832413e5a6abd271ca7b4c9ebe442f36c19db2d783906d423dddad73f0d75493ca2328d06877bb44712eef13e19261446db7baca757bc464ff00674c75cf3e852e631bb60c531ccfbc7a04d29b18e6107d8bc071c9d349a0c72cca1b652da20549d8b7e7eccf7fc0c007cd595edc9062d8ff00ee56971ff6ed777fa429318ddb102dbab09da8e45c07ccafaadab44fee2a75ab5e07401ae0b6c50b52d6b0b5d4ded3aecd13af539fc90ceb4047fdd9f07326a3c8779123d11741bb2c2d0c7b24fbb43f785d1bcb893c7e6a9bcb50dcdd46bf5754613fd216dd6d3aeb32e6868a55083900c780d11aed3a79aaaa59c443693632825ce7488f88febe6a154568d96d4786fdd01ce701c3b8d8f09551a60386750919f7984091d50db2f7da9272a05c4ef2f747912011e10a96520d969a6f699c852cc93a982aca97359e206d47fe9d3327fdc442bad30ea919b2b00728db0d711ccf0e88ec4255b398fdc1235efd404faa1efe853802a0aa09f85930075884d9f826d107d84471aae23c7bc6512dc36ae403c5368f85827e67f446641a0dd9da425c4031dd6e667404eb1f882b31a9ce35881d4ffca61656decc1cc924c9275d23f240dcd21509074cc9cc8f984bbecd233f5ad44b1921c401b47746e0380ff2a4ea227f8c472da6e5c930fd9d4c68c6cf1224f995cfb237eeb7c826b9978a4d6e43a0f442dcb131a145cf0365ae7643404eee4adfd8970fd28bfc46cff7424fc9ff000cf3daaec30439dd07aa78cec7ddbbe16b7f99edff00eb2afa7d81ac4cbab536f40e77e413969b61f43bac701991279f15dc5b0c6dc30e5151a0ec9e3f84a656b853e852631a4bf641ef6cc6f9d279f151ba3949969e23ddff0009e788bcdf0d2e654730e45a6798cd36bba81b0ff85c608e7f10fae2b576b86dbbaa17d4a4d3548d65d0474984c05ad1190a5480fe569f5086f54d374135c1d6ed703236599f800bcf70fa4e17170c0d71ef02365a0e7278e5c17a81d9027bbb2d20c00220104e4376457c313a6464f11cb44bbd6cda619987d62f1b34ea0e3fb933e7a04de8e1158e45ac399fe33585a0710d6ebe29e1c419cd5556f801207cd28965c609536a455a446f06806ba37c38130559fb24c107d901c3d9071f1738c956bf113c00553ef8f159906605487dee618ef66d3e0d53a9855bc6744759713e64a1aadcb8ef2abf6c7793e6b30e652a4d10d680382e7b560de12da9582a5d5901347de307c5eaa875fb389f24b1f590eeaa101d1bbafdbcd0f53136fd14b6e2ac64837ba5663617e5c4068cb7a8b9f0553873224f00079e7e80a16eee21c47d4ef45869aaabf6a806d524c05d3fcc3cd667abdbdff71a3f0b7d02f8de3782416f75dd6e7b87a2b3ed1cd6b6ed8dcdf8fba1769df12400067c927f6c131c0ea17d56b5a04ef3c06f28e3ddd053b7507ece664eb1984a315a4fd47a4f9f1445fd434ebbda1ce201de7e48abcb671a5ed4125bbc10411cfa2eab8689961a9b634dd41194721a751c3a27b86d32e67b43983a7e6524c41a5c72324e504673bb3dfe2b676059fbba54dcd7b43761d1c5a33779c9430c77763f381aadafb466d3400e0208dce1c0ac85fe055ade99aa605373bbb9cba0711bbfc2f53a1641834d77257dab653fb30a6e7343a6434913bf726cf0961b7bbd3cb28dd924665175ee88f9faa1a8e14f0e39e851173871710391f52b9f852dca2875d1e2a26e79afadf077139abdf833c027821c2b72814dd735075daf9f6c61055725b8d19655eeba54bae4a1df5150ea88687625f5f9afaddd2ecf4124f8209cf44d0c9bcdde83fcfa21a6dbae3265498d5f31bbd481404c6d84333df27c34fc8acfb65e4bb202492e3cf3f13c968ef76594897681bb206f2620f4d4e6b2e43ea9800068d00c9ad1c4fea53e10b6e93a975f0b321bcfc4efd0725116cfe1f353eed3c999bb7bff00fc8ddd553b053e836d634f744701e8a9359c37956d31dd1d07a2a2a05cf97aac9d3bf6a3be7cd3fec96314e856f68f7c340208824ba7878c2ccb934ecae19f68b86b1ced860cdeedfb2370e64c0470b794d1729d37b4432bd5f6ac21d4dce9998ca7307783c96aaeee1a29ba76764033272000e484fd88d000a64068d00facd637b558c063bd834cb5a66a91be34a43a9807ac715e86f5eb493e9d7e92c4710a625b4a83648ef38897091a027dd30772d3601529d4a61eda7b1b1dd91b248cb4d16270fa6f7376dfabdce2794ee5a3ecc56f64f7b5deebda7a6d0cc7e6124cbb2e796bfac198b56791dcb978e2d86b49e8e6899f14a6dd9ece5cd0368eae39bbfa8e681c56e3bc61470fc5c7f0dfa9f75df914d472994c7a0372f21e7c4a1fda39ce81d3e6a57b7076e136c1ac648295cce8a05ad08fb5b796191a84d0e1c084651b201a026d16b297781ed6822634eb9fa24f73d968d4ee1afcd7a29b5e087beb5910b7195a64f2dafd9d2368c69c124af631c57aaded00d6e7e2b397f62da9a0cd4f2c0f326045224c0de611444ba01c8653c86ffae29ed3ecc5c06beab29b9e1a0c6c89ccefcb805aac12d285bdb5368b763eb39a1d51f5181ce0e767b2011944c7824985ab63bbe3cddcee1a265616a1a5a5e33d43790ce5dcb2d37a6b8ed8b69560f6b0173da1cd601dd06482e2386432e2553855b197103da553971027593bd4ee3aa6a598a53da1b755d0099e6e8dcd09457b891b2d1b2de0353cdc7795baa98152076ee1ded1e346030d1d6333e09563745bb1dca6c6eec9a1a23994d3a89dac9264ca190c955469303849da3c069e251e6b8fbad4587d3100741e887a9ac234b3bad3c87a216e59bd432f5697a0ce53b1bd7d17ed34f51b88e0575f9e6aa6d393921373c0adde1dda771a6e2c71da0220ead2721d473e4b3f6b62f792e824498df2779fae687b1c1dee3398e616e707acda56fde82e6cb47a8cba15d986572f467d78cd4594b0e34edc39d9411cb54a6f717a4d1992237cc8f308ac57b554bd93a955786edb480e8c83be1278670bc72e6f6b5624898e3bbc38a37de90caeeede9d4f11a35892d702f1991be3ef0fcd16f7517b6765a4f412bc6455a949ed7b6a10e6991fe7883a11bc14c304ed154a4e879da613afddff0008d9741cef8f41a7436ea9e50b61845b8002ca617543a1e3785adc22a64250c4b4fa985279430a8a5ed15369e9634cab2ab042a286a89715a56acde3d40900059eab4881016cb1019159cba6e8240e677734b4d076078ddbd263e9d6a81bb5c9c738e411f84e3b6f9c540f3ba1ae98de3dd90b3f53b3b4ab89a2e0e2dc9c64833aef52b4eceb183bc4bb9124b7ca611dd757fd7c55f6a6c5f5ee0bc9d8a4435ac2cef3ded19902326e6e3ae6b8cc35cc606d2a669b3a105dcc93994eb09c0f669b9c7ba09daa79996c6ae807423d132b9c2e5a3bd235278cefe89786eecba9fb616b34b2769a5dcf41f2d561fb41891aaff89a0641a7411bd6f3b5b5cb6732d3f0e5ab4653075192f33bfb8739d9eb3b9472eae9a61b59878cc9e1f9a30aab0bb571cb7929cfec7fc47e486cba1a0770741e8a8af108b637bade83d10b74ccd4f2f5480837347e1f661ee066235e0aaa54a56930eb30d6c989dfc826f9e3ba5cf2d4097774f6ff00081207cf98210b42f6b3f22df9c260e2d06065c137b3b486174098274e4ba748ede698f5ad47d41b7933ae67ea167aeb1a0c7ecd368869cc9120c6e8e0b5b8a572faaf9e397201a1adfed5e66e993c64faa6c3185cab4b8ae2bf6dad5abd434693b603b6402d6bcb4319b0c19f788ef41d60a42e39e4aa2d5f526926153513dbd2ff00d3abe2f69a673d9d3a1d17a65ab6005e53fe96d2ff00a923f07a10bd969d0c94b5da96a2cacae63973d82e6c908e8a298e85da95c20f6f8aaabdc0dcb6c53b8a9912b357efd779dc995d5e08859bbabad4ce4932a7c62cc26f6a30bd80c6d9127909903aceab42dadb2de392f39b8ed351a27225c7f0e7f35437b7a0980d201ca49426ccf43ffb4550d469274ddbba422306c69d4dbb261ec68f71c608fe43bc7e13e1c163ed2fc55870d767d422cb8828dcd4dcbe16f6e71b355c4b8f7db04019000c80d0b256b477ef289ed05db6a5c64d1fcc0ebb3dd1234dc8bc3a902a39fec6530c26d48e53e709b7b051b16e528d8530da5895b6cecb87baf634f29d913faf8a55714f25a1b08ad6e19deda68046d35cdce33024673d52b34b87f908df4678ab0ab225c09d3529d97cb5c46ef442e096c65c26792615edb61ae023357f9ce92cef6cd56ae36b867af0ff0009fe0589071d871e4b39754ccfd410a5696a4e609907220e63eb9aa13659da6c39d42e0ba3b8753b8b773bc3458cc5f0dd879737dd719e84ea17b353b575566cd40da8deb0e1cd24bfec2d4ffc2cda7e170d3a384a3373b0bdc7921b571dc8bb3b1cfebc96fa97fa7b74e747b30071253bc3bfd38a8c82f3b4edc23ba39ca6b95a4909ff00d31b322e5c63e1d9f9caf64a545677b3fd95fb33b6891bf4e2752b50c4718d9547d928bed82b8b971ae4c52ab9a11aa4f76085a8ae4149ef6ca730a7943cac9624e30bcd3b4d8e17934e99860d48f88fe8b7bdb5b834e93c68e881e2bc96a885393754b751f36c9fb2ca8e05b49cf2c152096cb764bc65a901c0c2b6a59804ecb83c0240709d978060384e601d7340567988931331ba78c7157e155c876c9cc1f52a967449976d6f652e440041cbf55a5c52e9ada6e783eeb49dfaee590c15db2e29d622c3568ecb7392d9e80c9f40b9efabc22b4b57d53b40340dc6332b4187e18f2434193c82b2c6cf65a0468345a9ecdd268127de24f90dc96f747c8aedf057b5ba8e8ab75074ad6399ba3c6442a0db1fbbe8b7185959bb5ba73431c24c35a336f773033eec2331068caa35a21dafbd93b7ef1aa5b6ce1b2c6911dd11c0e5a6f4d6c5d2361d041ca47cb2d01432f4f3c1f835007bd034e79a32e68ccaa6c7f7608cf24b711c660e415f0f11cbd2dc470a78748062776ef046619851c88241e310afb5bba9506e1d735a4c3e9437329e4d92d76c6c200da82534a6d010ded1594092a9a2516d2175ce55caa9ce441c7d45d6d50975ee22d698d0f35452afbe52ed4986ce4d508775c25b52f020ea5e21723e3f33cda95597c1415bde8dea552b89d75436171d323fea0e0b51fdf6896467f84ee2bc8b12b6731c5ae0411bbf3e8bf4b5b77a41120eab2ddacec2b2ab76a98cc691ef0e42751c90d7e4b6bf3fb98494c30dc38821e72038ef3b805a9b9ec8d4a67de603f89a5bfaa853c13306ad66903734ca172fc34c51b7b20ea61c6732730754c30aa3b2626415daf50101ac6e437e811387d3e2a5674ab438561e0fbda26975625bdea7901a8fcd72c2a80d09832e1ba6a93a35b4b29079399cd15ec9dc5326d111935be6015c34cfddf9ada0d9153c289630cbf46e4d7348397e27c0e9055adc19e082194c113992c91fd2e12b67676edf66ceeb7dc6fc23ee856fd919f75be4153fc65e6cd0b72009ccc4188ce37e44faa4b89614e2e96ade55b6681900167ef283a624fcd3c9a2dbb2ac3f0f7832e68ea4ad1b19020109236c8ce64f8903d53bb1868ca0f8ca7c49908a54cef44b5d0a9174a62b85422ea6f0549e021d959bb94c545999fed4d96dd3740cd62a976ac52696560e0e6e5206a06feabd36b9075584ed7767c3e5cc19a9d747cb2fda9c2bb454abc863b31ab4e4638a626e02c261d869a554388884ec5e67aa9de9db31db42eba037afa9de73587bac56b3df14f21397129fe0946abe0d4d3871425a9fd2491bbc2abc84df6a424586514d730ad2b8720188d8876b0b2b8b61c07c3e50b597356647aa438853201ef98e112968c63eb520dd0cf50bb6748abee69e73048e28ac368ed3da275f924b4f036cddb092c68737703910beab895db636a8473da3fa2d854c306fa8ff000202853c0d9be4f52573d96af2c67adb1faa465461dcdf97a2b3ed979ff996c3977b2ffe4b48dc128fddfaf1567ec7a3f74790fd10d50dc68ac2a7ee9903e06f2f842b4876f202aacddfbb67f237fb42bcb0ee1e6bbb4e5daa34c73293e25409339c754ffd97127c32435d526c7d12b716db2bec9a3e1f3ff28cb4bb8ca1a07255deda41cf21f3411781935a49e24c24f05a0170d3ac291a40ef48adae40f78cf4fd5182bb2324f320b069b7e6a0d1b2354b4d574eb92aaad770dff3479068c9f539aa6ae6083a25353112348405d5e38ef296e50f3129ed231cc71e1c527b46b9c99e2550bc808fc3e880dc824d3ae7d2cc5dc3f0c020919a7541a02129551d0a26922e7cb2b4f70bab0611f52b1988959da5b4dd0a329ddbf789e68ca9d826b89ea93e27472d0f51b9356de6e210f883b2969206f883f27647e48568c91ac01839f34db04a12edadc07cca19d69b6e10da6e939c6d537f8b418f295a8b2b06b1a000429d523aca6af5c705cda4a6da4baa92fe0a3b678ac07d6440a74ff919fda15aeadc020eca4d3a73f75bfda1174dbff0bb1cc8004a91600ac7d455b5b9c9f2599454a22a6ec967f13b52dc96a65666bd70f738f0739bfd248fc94fe9d1f09b2434e17056211d5834e8503b20cc152e4ae96b6acaaea286cc290d16e41a035aa41d12bbaba27927afa3281ab6128ecd2917b42539c3ee24415c38746e5632c4a3b1b576c29d3da0ada340ef57b2921c8a8d0b870e68fa775921d8c089f662010b6c34bbdb8220e7ea3a1550b335090d2a54ecdced3cf726f42c030f3dc77742b4ec2f4070ec1b60cb8c9dc9954a708a111f5f4545a11e303903502512fa4351a70e0a9ab4e54ecd292ed4c03a2afd9a203217db280aea18ab1ac6012eeeb7a6838a2adaf054d01c9636c2e0b994c467b2dcf87742da5a520d608d79af473c6631ca23aafb551daf0e2a6d30a4cadef858dbe76c5578dc5ce3fd59cfcd686f5d5493ecda0f53092e2f68f0039e0027819d14be9dc53e7e8306424770f349e6a4f71c61e38468e4ed8cee92350966276ee7b5ed110759e62172ddcaebd4a9bae0c645769de7159bc1efcb4fb27ee2434f0e4537739a0c9dc8cb4971d1bb6b354db51a94b2bb0c448eaa6faed1cd1e5a2f135962934b772502e8705f53ba13047915b9371362e1c5752d024f244b1e8736e224b8052b4b807a1405db8c18dd1eabec264b5de7e792d32d9f87f5d9f61d7858ed93bfd7827546ab5e3233f972596782e682351ea34465ade443f8e4e1cd571a858d086f1f02b8e7eedff23d1428dcb5c322a50343e07eb7a74dc689cd5759a8869ce3e8ae5462d66c65024ae2b6a53f3f5e6abd952b34acbb4302c39aca6d2eccecb7a681370772a2c7f84cdddc6ff6856972ecb6d73ae6850aaf5c93c3251aa32419c6f35462b43da52c86998fd15a049e8bac77925a33a60ad6b07830608991e305114e9f74c904985f769b0e34aa0a8d1dc71ddf093a83ea85b671da0b83eb75969df8c971dc64312a1b359fcf31e2995a12e609d57313a53739e905196d6fb211c6b7d3d428d1339a2219c2571d9eab89f5b4ad7cfa837085ca34e4ae6c839abedca166a0cb57b4429b5ca04ae35ca369a47d70ff00787e1289c35bb20703979a02d0ed55e507d1346888854c61beb7538a74dfde2de3f41594ea89cc407483c8aa2be4e9e39a935d2638e63aaab984309614c6d6fa3271cb8fe6966d6d3798d7a2835f223cbaf05b61a6ae9560edeac0771d564a8d52d3aa6b4312819e7c13cc8bc4d1ec2aacf80555adf87a29333eb47feed83f0b7d02b0e487b33fbb67f2b7fb422299939e8ac9a6c71537b44415c2f12a3b6b6d9c7903964a01db949f9e45721251575a8878d9398dfcd65712c28d176d03dd339f0e4b5a0478aaeb520f690448d14f3c26515c32b8bc9314aa76e46edebb4ef0c44ad4e39d9acc9a7a70deb257562e615c971eddb8e58e71235ddc549b7677a14172b6325bfbcfcb5f90b65d8465b561c52a6b532c34b6735b9644ff0018b067a2aaad61a0d576adcee0172dadb394bc7bd9a4e3374561b400328d555119c7256b0ee56c674e7cb2dd72e4f73a1f540dd5e060128ed8ddb8ff00ca518a52daeeee20f984f8cdd224fc76362a3448261de1a8ea4668efb634bf646a60b798e2162ec093b74cef123f987eb98465b3cbe9e53b74b4e258499f23eaaf7e70bb6d698da127a1fd571a60c25f84deedb44fbc35e69edad00ed146e3aba36c3d3699c9376d4742952b48457b34d216936218db2de8b37bb61bb2de27646bc963f10ed5d7a8e0012c11a349009de522c4b16350ed0d20068d7767e687a40939e7c7924fa675ef7f17f878618cb66eb5169dacb804cbb6b4001190cf53e00af45b0a9b4c692732013bb50bc669b5c321c57a9765df55d481a8234d91be00024adf2caed0ff0091f8618e132c6487ac2be79500545cf5d1b78ab0b9443942574372cd0a2a1c64f82c963553bc65a083e1e4b60d670497b4767b4d040dca5963b87c6e994d9a51bc7cd55f6369d1e3cd42b538d50e3552d2f3e99415f603cbcd5b6f62415ca4fd13361f45b437ed92b16c15cc600725f4e61480ccf41f9a3c53b95ab2980012a2c72930aaea044ab5db8f3425e9d93c81ff000aeda52ac37f15a3560eab8b6a48ca1df9a369bb62a877c2eccf02d7e447aa862f685af770d49e441fd14a877a967f01f93bfc8f9aec97712a6b85d02caa5b39007c41d0fa2d6e1953349f0ea60d36be3380277c0d130b47c101473f4d3c69db98502d7705f5bc442b64ac0f08a236581c780f9e88d0e8020499cf965f5e69703953f1fed28ca3abbafe417366fadc3c8def636cd8e6fb5d91b43bb273cf52ef281e6b60c0b33d861ff4c3f99deab4a574fcbff31f37fcdcedfb65fe9c2ecb5548ab215b5b77455db8f529ab922e9cc29555cdebaf586b8c3f92aae19b513cd5ac5f55d166db1f8fe1064b9a3259fa76f9e6bd22a8c964f12680e390fa0a596279913bd90995039212aa2a868a70d57355b5bdd2a9a7f9abb726646720ba0ca8535c6fbc7c16648d320eaa550e414ab7ba7a150a5a1eab314e32d0593c123b476cb837ef8d9f18907cc04f719fe0bfeb7ace5e64fa3f5bdaaff3f095abecfddcb0b5d9ee4da99ddc167309fe23fc7d568e96be4933f461ee1af96e7b9320e09661deea20ad0b5fffd9, '2026-05-08 08:42:32'),
(2, 4, 'John Doe', '09123456789', NULL, NULL, NULL, NULL, '2026-05-08 09:20:31');

-- --------------------------------------------------------

--
-- Table structure for table `queue_form`
--

CREATE TABLE `queue_form` (
  `form_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `first_name` varchar(20) NOT NULL,
  `middle_initial` varchar(4) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `age` int(11) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `purpose` varchar(100) NOT NULL,
  `symptoms` varchar(255) NOT NULL,
  `patient_type` varchar(40) NOT NULL,
  `contact_number` varchar(30) NOT NULL,
  `form_type` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `queue_form`
--

INSERT INTO `queue_form` (`form_id`, `user_id`, `first_name`, `middle_initial`, `last_name`, `age`, `gender`, `purpose`, `symptoms`, `patient_type`, `contact_number`, `form_type`) VALUES
(1, 1, 'Venz Virni', 'T', 'Blanza', 20, 'Male', 'X-ray', 'Hiwig Tiil', 'Person with Disability', '09919106710', 'Diagnostics and Laboratory'),
(2, 1, 'Sarah Mae', 'B', 'Sario', 21, 'Female', 'Postnatal Care', 'SSS', 'Regular', '09233211123', 'Women\'s Health'),
(3, 1, 'Sage Luther', 'B', 'Cui', 19, 'Female', 'Prenatal Check-up', '5 Months', 'Pregnant', '09919106710', 'Women\'s Health'),
(4, 1, 'Sage Luther', 'B', 'Cui', 19, 'Female', 'Prenatal Check-up', '5 Months', 'Pregnant', '09919106710', 'Women\'s Health'),
(5, 1, 'Sage Luther', 'B', 'Cui', 19, 'Female', 'Prenatal Check-up', '5 Months', 'Pregnant', '09919106710', 'Women\'s Health'),
(6, 1, 'Venz Virni', 'T', 'Blanza', 20, 'Male', 'Check-up', 'SDASD', 'Person with Disability', '09671103230', 'General Wellness'),
(7, 1, 'Sarah Mae', 'B', 'Sario', 21, 'Female', 'Family Planning Consultation', 'Ambot uroy', 'Regular', '09919106710', 'Women\'s Health'),
(8, 1, 'Juner John', 'V', 'Caimor', 21, 'Male', 'ENT (Ear, Nose, Throat)', 'Runny', 'Regular', '0988231202', 'Specialized Fields'),
(9, 1, 'Sarah Mae', 'B', 'Sario', 21, 'Female', 'Breast Examination', 'WOW', 'Regular', '23123123123123', 'Women\'s Health'),
(10, 1, 'Sage Luther', 'B', 'Cui', 19, 'Female', 'Prenatal Check-up', 'Pregnant', 'Pregnant', '09919106710', 'Women\'s Health');

-- --------------------------------------------------------

--
-- Table structure for table `queue_line`
--

CREATE TABLE `queue_line` (
  `queue_id` int(11) NOT NULL,
  `form_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `department` varchar(100) NOT NULL,
  `queue_number` varchar(20) NOT NULL,
  `status` varchar(30) DEFAULT 'Waiting',
  `created_at` datetime DEFAULT current_timestamp(),
  `staff_assigned` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `queue_line`
--

INSERT INTO `queue_line` (`queue_id`, `form_id`, `user_id`, `department`, `queue_number`, `status`, `created_at`, `staff_assigned`) VALUES
(1, 1, 1, 'Diagnostics and Laboratory', 'D-01', 'Completed', '2026-05-04 19:54:32', 'Dr. Shaun Murphy'),
(2, 2, 1, 'Women\'s Health', 'W-01', 'Cancelled', '2026-05-04 20:00:31', 'Dr. Gregory House'),
(3, 5, 1, 'Women\'s Health', 'W-02', 'Waiting', '2026-05-04 21:08:45', ''),
(4, 6, 1, 'General Wellness', 'G-01', 'Waiting', '2026-05-04 21:11:34', ''),
(5, 7, 1, 'Women\'s Health', 'W-03', 'Waiting', '2026-05-04 21:19:51', ''),
(6, 8, 1, 'Specialized Fields', 'S-01', 'Waiting', '2026-05-04 21:22:02', ''),
(7, 9, 1, 'Women\'s Health', 'W-04', 'Waiting', '2026-05-04 21:33:58', ''),
(8, 10, 1, 'Women\'s Health', 'W-01', 'Waiting', '2026-05-05 13:05:44', '');

-- --------------------------------------------------------

--
-- Table structure for table `schedules`
--

CREATE TABLE `schedules` (
  `schedule_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `day_of_week` tinyint(4) DEFAULT NULL,
  `specific_date` date DEFAULT NULL,
  `time_slot` varchar(20) NOT NULL
) ;

--
-- Dumping data for table `schedules`
--

INSERT INTO `schedules` (`schedule_id`, `service_id`, `day_of_week`, `specific_date`, `time_slot`) VALUES
(1, 1, 1, NULL, '8:00 AM'),
(2, 1, 1, NULL, '9:00 AM'),
(3, 1, 1, NULL, '10:00 AM'),
(4, 1, 1, NULL, '1:00 PM'),
(5, 1, 1, NULL, '2:00 PM'),
(6, 1, 2, NULL, '8:00 AM'),
(7, 1, 2, NULL, '9:00 AM'),
(8, 1, 2, NULL, '10:00 AM'),
(9, 1, 2, NULL, '1:00 PM'),
(10, 1, 2, NULL, '2:00 PM'),
(11, 1, 3, NULL, '8:00 AM'),
(12, 1, 3, NULL, '9:00 AM'),
(13, 1, 3, NULL, '10:00 AM'),
(14, 1, 3, NULL, '1:00 PM'),
(15, 1, 3, NULL, '2:00 PM'),
(16, 1, 4, NULL, '8:00 AM'),
(17, 1, 4, NULL, '9:00 AM'),
(18, 1, 4, NULL, '10:00 AM'),
(19, 1, 4, NULL, '1:00 PM'),
(20, 1, 4, NULL, '2:00 PM'),
(21, 1, 5, NULL, '8:00 AM'),
(22, 1, 5, NULL, '9:00 AM'),
(23, 1, 5, NULL, '10:00 AM'),
(24, 1, 5, NULL, '1:00 PM'),
(25, 1, 5, NULL, '2:00 PM'),
(26, 2, 1, NULL, '8:00 AM'),
(27, 2, 1, NULL, '11:00 AM'),
(28, 2, 1, NULL, '3:00 PM'),
(29, 2, 3, NULL, '8:00 AM'),
(30, 2, 3, NULL, '11:00 AM'),
(31, 2, 3, NULL, '3:00 PM'),
(32, 2, 5, NULL, '8:00 AM'),
(33, 2, 5, NULL, '11:00 AM'),
(34, 2, 5, NULL, '3:00 PM'),
(35, 3, 2, NULL, '9:00 AM'),
(36, 3, 2, NULL, '10:00 AM'),
(37, 3, 2, NULL, '2:00 PM'),
(38, 3, 4, NULL, '9:00 AM'),
(39, 3, 4, NULL, '10:00 AM'),
(40, 3, 4, NULL, '2:00 PM'),
(41, 4, 1, NULL, '9:00 AM'),
(42, 4, 1, NULL, '11:00 AM'),
(43, 4, 1, NULL, '1:00 PM'),
(44, 4, 3, NULL, '9:00 AM'),
(45, 4, 3, NULL, '11:00 AM'),
(46, 4, 3, NULL, '1:00 PM'),
(47, 4, 5, NULL, '9:00 AM'),
(48, 4, 5, NULL, '11:00 AM'),
(49, 4, 5, NULL, '1:00 PM'),
(50, 5, 2, NULL, '10:00 AM'),
(51, 5, 2, NULL, '2:00 PM'),
(52, 5, 2, NULL, '3:00 PM'),
(53, 5, 4, NULL, '10:00 AM'),
(54, 5, 4, NULL, '2:00 PM'),
(55, 5, 4, NULL, '3:00 PM'),
(56, 6, 1, NULL, '8:00 AM'),
(57, 6, 1, NULL, '1:00 PM'),
(58, 6, 1, NULL, '4:00 PM'),
(59, 6, 2, NULL, '8:00 AM'),
(60, 6, 2, NULL, '1:00 PM'),
(61, 6, 2, NULL, '4:00 PM'),
(62, 6, 3, NULL, '8:00 AM'),
(63, 6, 3, NULL, '1:00 PM'),
(64, 6, 3, NULL, '4:00 PM'),
(65, 6, 4, NULL, '8:00 AM'),
(66, 6, 4, NULL, '1:00 PM'),
(67, 6, 4, NULL, '4:00 PM'),
(68, 6, 5, NULL, '8:00 AM'),
(69, 6, 5, NULL, '1:00 PM'),
(70, 6, 5, NULL, '4:00 PM');

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `service_id` int(11) NOT NULL,
  `service_name` varchar(100) NOT NULL,
  `service_type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `services`
--

INSERT INTO `services` (`service_id`, `service_name`, `service_type`) VALUES
(1, 'Check-up', 'General Wellness'),
(2, 'Vaccination', 'General Wellness'),
(3, 'Postnatal Care', 'Women\'s Health'),
(4, 'Pediatrics', 'Specialized Fields'),
(5, 'Dental', 'Specialized Fields'),
(6, 'X-Ray', 'Diagnostics & Laboratory');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('patient','admin') DEFAULT 'patient',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password`, `role`, `created_at`) VALUES
(1, 'caimorjuner@gmail.com', '123123', 'patient', '2026-05-01 11:16:19'),
(2, 'admin@admin.com', '123456', 'admin', '2026-05-04 03:47:37'),
(4, 'johndoe@gmail.com', '123123', 'patient', '2026-05-08 09:20:31');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`patient_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `queue_form`
--
ALTER TABLE `queue_form`
  ADD PRIMARY KEY (`form_id`),
  ADD KEY `fk_user_id` (`user_id`);

--
-- Indexes for table `queue_line`
--
ALTER TABLE `queue_line`
  ADD PRIMARY KEY (`queue_id`),
  ADD KEY `fk_form_id` (`form_id`),
  ADD KEY `fk1_user_id` (`user_id`);

--
-- Indexes for table `schedules`
--
ALTER TABLE `schedules`
  ADD PRIMARY KEY (`schedule_id`),
  ADD KEY `service_id` (`service_id`);

--
-- Indexes for table `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`service_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
  MODIFY `patient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `queue_form`
--
ALTER TABLE `queue_form`
  MODIFY `form_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `queue_line`
--
ALTER TABLE `queue_line`
  MODIFY `queue_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `schedules`
--
ALTER TABLE `schedules`
  MODIFY `schedule_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `services`
--
ALTER TABLE `services`
  MODIFY `service_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `patients`
--
ALTER TABLE `patients`
  ADD CONSTRAINT `patients_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `queue_form`
--
ALTER TABLE `queue_form`
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `queue_line`
--
ALTER TABLE `queue_line`
  ADD CONSTRAINT `fk1_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `fk_form_id` FOREIGN KEY (`form_id`) REFERENCES `queue_form` (`form_id`);

--
-- Constraints for table `schedules`
--
ALTER TABLE `schedules`
  ADD CONSTRAINT `schedules_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
