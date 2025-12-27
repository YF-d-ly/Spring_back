    @Override
    public PageResult<GoodVO> pageQuery(GoodsPageQueryDTO queryDTO) {

        // 1. 设置分页参数
        Page<Goods> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();

        // 添加货品名称条件（模糊查询）
        if (StringUtils.hasText(queryDTO.getName())) {
            queryWrapper.like(Goods::getName, queryDTO.getName());
        }

        // 添加类别条件
        if (StringUtils.hasText(queryDTO.getCategoryId())) {
            queryWrapper.eq(Goods::getCategoryId, queryDTO.getCategoryId());
        }

        // 添加仓库条件 - 确保这个过滤条件正确应用
        if (StringUtils.hasText(queryDTO.getWarehouseId())) {
            queryWrapper.eq(Goods::getWarehouseId, queryDTO.getWarehouseId());
        }

        // 添加价格范围条件
        if (queryDTO.getMinPrice() != null) {
            queryWrapper.ge(Goods::getPrice, queryDTO.getMinPrice());
        }
        if (queryDTO.getMaxPrice() != null) {
            queryWrapper.le(Goods::getPrice, queryDTO.getMaxPrice());
        }

        // 3. 执行分页查询
        Page<Goods> goodsPage = goodsMapper.selectPage(page, queryWrapper);

        // 4. 查询分类和仓库信息（用于映射名称）
        List<Category> categories = categoryService.list();
        List<Warehouse> warehouses = warehouseService.list();

        Map<String, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        Map<String, String> warehouseMap = warehouses.stream()
                .collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));

        // 5. 转换为VO
        List<GoodVO> goodsVOList = goodsPage.getRecords().stream().map(goods -> {
            String categoryName = categoryMap.getOrDefault(goods.getCategoryId(), "未知类别");
            String warehouseName = warehouseMap.getOrDefault(goods.getWarehouseId(), "未知仓库");

            return GoodVO.builder()
                    .id(goods.getId())
                    .name(goods.getName())
                    .image(goods.getImageUrl())
                    .price(goods.getPrice())
                    .stock(goods.getStock())
                    .description(goods.getDescription())
                    .categoryId(goods.getCategoryId())
                    .categoryName(categoryName)
                    .warehouseId(goods.getWarehouseId())
                    .warehouseName(warehouseName)
                    .build();
        }).collect(Collectors.toList());


        // 6. 构建分页结果
        return new PageResult<>(
                goodsPage.getTotal(),
                goodsVOList,
                goodsPage.getPages(),
                goodsPage.getSize(),
                goodsPage.getCurrent()
        );
    }