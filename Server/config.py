#!/usr/bin/env python
# coding:utf-8
# 配置文件
import os


class Config:
    table_name = 'account'
    database_name = 'data.dev'
    database_basedir = os.path.abspath(os.path.dirname(__file__))
    database_abs_path = os.path.join(database_basedir, database_name)

    app_key = u'01d93632e5886f1145431c1e'
    master_secret = u'6a7f9e8c5c248d00b401dca6'


class DevelopmentConfig(Config):
    database_name = 'data-dev.sqlite'
    database_abs_path = os.path.join(Config.database_basedir, database_name)


class TestingConfig(Config):
    database_name = 'data-test.sqlite'
    database_abs_path = os.path.join(Config.database_basedir, database_name)


config = {
    'development': DevelopmentConfig,
    'testing': TestingConfig,
    'production': Config,

    'default': DevelopmentConfig
}
config_default = config.get('default')
