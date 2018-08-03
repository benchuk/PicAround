EXEC sp_attach_db 
@dbname = N'Picup',
@filename1 = N'C:\Android\WebSite\PicUpMvc3\PicApp1\App_Data\PicUpDB.mdf'

EXEC sp_detach_db 
@dbname = N'Picup'


